package server;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class DynamicElement {
	int cliIndex;
	String name;
	String value;
}

class DynamicTable {
	private DynamicElement dynaElem = new DynamicElement();
	private Vector<DynamicElement> dynaTable = new Vector<DynamicElement>();

	public int getSize() {
		return dynaTable.size();
	}

	/**
	* elimina tutti gli elementi inseriti in tabella
	*/
	public synchronized void clearTable() {
		dynaTable.clear();
	}

	public synchronized int getClientIndex(int id) {
		if (id > dynaTable.size() || id < 0)
			return -1;

		dynaElem = dynaTable.elementAt(id);
		return dynaElem.cliIndex;
	}

	public synchronized DynamicElement getElement(int index) {
		if (index > dynaTable.size() || index < 0)
			return null;

		dynaElem = dynaTable.elementAt(index);
		//System.out.println("DBG: index " + index + " varName=" + moni.varName);
		return dynaTable.elementAt(index);
	}
	
	public synchronized void setElement(int pos,String value) {
		DynamicElement dyna = dynaTable.elementAt(pos);
		dyna.value = value;

		dynaTable.setElementAt(dyna, pos);
		//sendBroadcastMSG(new DataFrame(NetConst.MONITOR_EVENT, index, s.length(),
		//		s.getBytes()));
	}
	public synchronized void addElement(int cliIndex, String varName) {
		DynamicElement dyna = new DynamicElement();
		dyna.name = varName;
		dyna.cliIndex = cliIndex;

		//System.out.println("addElement: clIndex: "+ cliIndex + "name: " + varName);
		dynaTable.add(dyna);
	}

	/*
	public synchronized void updateDynamicTable(int index, String varName)
	{
		for (int i=0; i < dynaTable.size(); i++)
		{
			dynaElem = (DynamicElement)dynaTable.get(i);
			//System.out.println("updateMonitorTable: varName " + moni.varName+ "/"+varName);
			if (dynaElem.name.compareTo(varName) == 0)
	        {
	        	//System.out.println("updateMonitorTable done: index="+index + " varName " + varName);
	        	dynaElem.cliIndex = index;
	        	dynaTable.set(i, dynaElem);
	        }
		}
	}*/
}

class ClientWorkerInfo {
	public ClientWorkerInfo()
	{
		clientID=0;
		bRunning = false;
		w = null;
	}
	public ClientWorker w;
	public int clientID;
	public boolean bRunning;
	public DynamicTable dynTable = new DynamicTable();
}

/**
* event manager: invia eventi (messaggi asincroni) ai client ogni 200ms.
* Lavora in bassa priorita' per favorire la ricezione delle richieste dei client.
*/
class EventManager implements Runnable {
	private static final int RUNNING = 1;
	private static final int TERMINATE = 2;
	private volatile int state;
	private SocketThrdServer theApp;
	private Random rd = new Random();
	private Thread _executor = null;

	EventManager(SocketThrdServer theApp) {
		this.theApp = theApp;
		//this.setPriority(Thread.MIN_PRIORITY);
	}

	public void start() {
		state = RUNNING;
		if (_executor == null) {
			_executor = new Thread(this);
			_executor.start();
		}
	}

	/**
	* chiude il thread
	*/
	public void stop() {
		state = TERMINATE;
		_executor = null;
       // theApp.finalize();
		System.out.println("Terminate");
	}

	public void run() {
		int NVARS = 30;
		
		while (state == RUNNING) {
			for (int index=0, ii=0; index < 1000; index+=NVARS) {
				String s = "";
				for (int i=0; i<NVARS; i++, ii++) {
					s = s + ii + "#" + String.valueOf(rd.nextFloat()/100) + "#";
				}
				//s = String.valueOf(rd.nextFloat());
				// evento di monitoraggio
				//System.out.println("MONITOR_EVENT: "+s);
				theApp.sendBroadcastMSG(new DataFrame(NetConst.MONITOR_EVENT, index, s.length(), s.getBytes()));
				Timings.sleep(70);
			}
		}
	}
}

/**
* trendevent: invia eventi di trend ogni 200ms.
*/
class TrendEvent implements Runnable {
	private static final int RUNNING = 1;
	private static final int TERMINATE = 2;
	private volatile int state;
	private SocketThrdServer theApp;
	private double translate = 0.0;

	TrendEvent(SocketThrdServer theApp) {
		this.theApp = theApp;
	}

	/**
	* chiude il thread
	*/
	public void terminate() {
		state = TERMINATE;
		System.out.println("Terminate");
	}

	public void run() {
		state = RUNNING;
		String s = new String();
		String sTime = new String();

		while (true) {
			if (state == TERMINATE) {
				System.out.println("Event Manager closed.");
				return;
			}
			Timings.sleep(200);
			translate++;
			Double d = new Double(translate);
			s = String.valueOf(d);
			theApp.sendBroadcastMSG(new DataFrame(NetConst.TREND_EVENT, 0, s.length(), s.getBytes()));
		}
	}
}

/**
* Attende richieste dal client e invia risposte
*/
class ClientWorker extends Thread {
	private static final int RUNNING = 1;
	private static final int TERMINATE = 2;
	private Socket client;
	private int clientID;
	private SocketThrdServer theApp;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected BufferedInputStream  objIn;
	protected BufferedOutputStream objOut;
	protected boolean bConnected = true;
	private volatile int state;

	ClientWorker(Socket client, int clientID, SocketThrdServer theApp) {
		this.client = client;
		this.clientID = clientID;
		this.theApp = theApp;

		try {
			objOut = new BufferedOutputStream(client.getOutputStream(), NetConst.PKGLEN);
			objIn  = new BufferedInputStream(client.getInputStream(), NetConst.PKGLEN);
		} catch (StreamCorruptedException sce) {
	  		System.out.println("Stream Corrupted Exception" + sce.getMessage());
	  	} catch (OptionalDataException ode) {
	  		System.out.println("Optional Data Exception" + ode.getMessage());
	  	} catch (IOException ioe) {
	  		System.out.println("i/o exception creating stream" + ioe.getMessage());
	  	}
	}

	/**
	* chiude il thread
	*/
	public void terminate() {
		// NB: se sono in read sospensiva sul socket,devo interromperla?
		state = TERMINATE;
	}

	/**
	* dato l'id del client ritorna l'indice della lista associata
	*/
	public int getIndexFromClient(int clientID) {
		for(int c=0; c < 10;c++) {
			if (theApp.clientWorkerList[c].clientID == clientID)
				return c;
		}
		// non trovato
		return -1;
	}

	/**
	* Invia messaggio al client
	*/
	public synchronized boolean sendMsgToClient(DataFrame msg) {
		try {
	    	objOut.write(msg.toBytes(), 0, NetConst.PKGLEN); //msg.getDataLen() + 6);
	    	objOut.flush();
	    } catch (EOFException eof) {
	    	System.out.println("eof encountered" + eof.getMessage());
	    	return false;
	    } catch (OptionalDataException ode) {
	    	System.out.println("OptionalDataException" + ode.getMessage());
			return false;
		} catch (IOException ioe) {
			// client sconnesso
		    System.out.println("IOException on write object");
		    System.out.println(ioe.getMessage());
		    System.out.println(ioe.toString());
			return false;
	    }
		return true;
	}

	final class Monitor {
		int id;
		String varName;
		String varValue;
		
		public Monitor(String varName, String varValue) {
			this.varName = varName;
			this.varValue = varValue;
		}
	}
	
	ArrayList<Monitor> monitorTable = new ArrayList<Monitor>();
	private void loadMonitorTable(DataFrame msg) {
		String monitorFile = new String(msg.getData(), 0,msg.getDataLen());
		System.out.println("load monitor table:" + monitorFile);
      
		try {
			FileReader fin = new FileReader("c:/sp6000/plc/"+monitorFile);
			BufferedReader in = new BufferedReader(fin);
			String s = "";
			
			try {
				String tmp[];
				while((s = in.readLine()) != null) {
					tmp = s.split(" ");
					Monitor m = new Monitor(tmp[0], "0.0000");
					if (!monitorTable.contains(m)) {
						monitorTable.add(m);
					}
				}
				in.close();
				fin.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

		sendMsgToClient(new DataFrame(NetConst.LOADMONITORTABLE_REQ,
              msg.getSubCmd(),
              0,
              null));
	}
	
	private void sendAllMonitor() {
		System.out.println("sendAllMonitor...");
		int index = 0;
		for(Monitor m : monitorTable) {
			theApp.sendBroadcastMSG(
					new DataFrame(NetConst.MONITOR_EVENT, 
					index, m.varValue.length(),
					m.varValue.getBytes()));
			index++;
		}
	}
	/**
	* loop di attesa richieste
	*/
  	public void run() {
	    String line;
	    int cnt=0;
	    String s;
	    DataFrame msg = new DataFrame();
		int idx = 0;

	    System.out.println("ClientWorker...");
		state = RUNNING;
        byte[] dataMSG = new byte[NetConst.PKGLEN];
        int rb = 0;

	    while (true) {
	    	if (state == TERMINATE)
	    		return;

	    	try {
                if ((rb = objIn.read(dataMSG, 0, NetConst.PKGLEN)) < NetConst.PKGLEN);
				msg = new DataFrame(dataMSG);
		    }
		  	catch (EOFException eof) {
		    	System.out.println("eof encountered" + eof.getMessage());
		    	break;
		    }
		  	catch (OptionalDataException ode) {
		    	System.out.println("OptionalDataException" + ode.getMessage());
		    	break;
		    }
		  	catch (IOException ioe) {
		  		// client scollegato
			    System.out.println("IOException on read object");
			    System.out.println(ioe.getMessage());
			    System.out.println(ioe.toString());
			    theApp.disconnectClient(clientID);
			    return;
		    }

		    // dispatch delle richieste
			switch (msg.getCmd()) {
				case NetConst.ENABLEMONITOR_REQ:
				{
					byte[]  b = BinaryConverter.shortToBytes((short)msg.getSubCmd());
					int enable = b[1];
					//System.out.println("Enable Monitor: "+enable);
					 sendMsgToClient(new DataFrame(NetConst.ENABLEMONITOR_REQ,
                            msg.getSubCmd(),
                            0,
                            null));
					
					if (enable == 16) {
						//theApp.evntManager.stop();
					} else {
						sendAllMonitor();
					}
				}
					break;
					
				case NetConst.GETALLMONITORVALUES_REQ:
					sendMsgToClient(new DataFrame(NetConst.GETALLMONITORVALUES_REQ,
                            1,
                            0,
                            null));
					break;
				case NetConst.FORCEPLCBLOCK_REQ:
					sendMsgToClient(new DataFrame(NetConst.FORCEPLCBLOCK_REQ,
                            1,
                            0,
                            null));
					break;
				case NetConst.GETLASTACTIVEPLC_REQ:
					String plcName="fileplc";
					sendMsgToClient(new DataFrame(NetConst.GETLASTACTIVEPLC_REQ,
	                            1,
	                            plcName.length(),
	                            plcName.getBytes()));
					break;
				case NetConst.GETPLCSTATUS_REQ:
					  sendMsgToClient(new DataFrame(NetConst.GETPLCSTATUS_REQ,
                            1,
                            0,
                            null));
					break;
                case NetConst.LOADMONITORTABLE_REQ:
                    loadMonitorTable(msg);
                    break;
				case NetConst.LOADABS_REQ:
					sendMsgToClient(new DataFrame(msg.getCmd(), 0, 0, null));
					Timings.sleep(5000);
					sendMsgToClient(new DataFrame(NetConst.LOADABSOK_EVENT, 0, 0, null));
					break;
				case NetConst.CONNECT_REQ:
					// connect
					//theApp.connectClient();
                    System.out.println("CONNECT_REQ");
					if (!sendMsgToClient(new DataFrame(msg.getCmd(),
													   0,
													   0,
													   null)))
					{
						theApp.disconnectClient(clientID);
						bConnected = false;
					}
					
					// sempre master 
					sendMsgToClient(new DataFrame(NetConst.MASTER_EVENT,
                             0,
                             0,
                             null));
				break;
				case NetConst.KEEPALIVE_REQ:
					// keepalive
					if (!sendMsgToClient(new DataFrame(msg.getCmd(),
													   0,
													   0,
													   null)))
					{
						theApp.disconnectClient(clientID);
						bConnected = false;
					}
				break;
				case NetConst.LOADDYNAMICTABLE_REQ:
				{
					// carica tabella di visualizzazione dinamica
						String fileName = new String(msg.getData(),
								0,
								msg.getDataLen());
					//theApp.plc.AddDynamicVarFromFile(fileName);

					if (!sendMsgToClient(new DataFrame(msg.getCmd(),
													   0,
													   0,
													   null)))
					{
						theApp.disconnectClient(clientID);
						bConnected = false;
					}
				}
				break;
				case NetConst.REMOVEDYNAMIC_REQ:
				{
					int i = getIndexFromClient(clientID);
					theApp.clientWorkerList[i].dynTable.clearTable();

					if (!sendMsgToClient(new DataFrame(msg.getCmd(),
													   0,
													   0,
													   null)))
					{
						theApp.disconnectClient(clientID);
						bConnected = false;
					}
				}
				break;
				case NetConst.FORCEONE_REQ:
				{
					/*
					if (!SocketThrdServer.bInitOpenPageRunning)
					{
						// forzatura singola variabile
						s = new String(msg.getData(),
								0,
								msg.getDataLen());
						int pos = s.indexOf('=');
						if (pos != -1)
						{
							String varName = s.substring(0, pos);
							String varValue = s.substring(pos+1);
							int id = getIndexFromClient(clientID);
							for(int i=0;i<theApp.clientWorkerList[id].dynTable.getSize();
								i++) {
								DynamicElement dynaElem = 
								(DynamicElement)
								theApp.clientWorkerList[id].dynTable.getElement(i);
								if (dynaElem.name.compareTo( varName)== 0) {
									dynaElem.value = varValue;
									theApp.clientWorkerList[id].dynTable.setElement(i,varValue);
								}
							}
							short ret = 1;
							if (ret == 0)
							{
								short[] errCode=null;
								String[] errMsg=null;

								System.out.println("error message Unknown");
								System.exit(-1);
							}
						}
					}*/
					sendMsgToClient(new DataFrame(msg.getCmd(),
												   0,
												   0,
												   null));
				}
				break;

				case NetConst.GETDYNAMIC_REQ:
				{
					if (!SocketThrdServer.bInitOpenPageRunning)
					{
						// richiesta valori dinamici
						short index = -1;
						Random rd = new Random();
						s="";
						int i = getIndexFromClient(clientID);
						for (int j=0; j<theApp.clientWorkerList[i].dynTable.getSize();j++) {
							// ricavo l'indice client
							int cliIndex = theApp.clientWorkerList[i].dynTable.getClientIndex(j);
							if (cliIndex != -1) {
								//s = s+cliIndex+"#"+String.valueOf(rd.nextFloat())+"#";
								//theApp.clientWorkerList[i].dynTable.
								DynamicElement dynaElem = 
									(DynamicElement)
									theApp.clientWorkerList[i].dynTable.getElement(j);
								s = s+cliIndex+"#"+dynaElem.value+"#";
								if (!sendMsgToClient(new DataFrame(NetConst.DYNAMIC_EVENT,
													   cliIndex,
													   s.length(),
														s.getBytes()))) {
									theApp.disconnectClient(clientID);
									bConnected = false;
								}
							}
						}
					}
				}
				break;

				case NetConst.ADDDYNAMICVAR_REQ:
				{
					// inserimento (append) variabile dinamica in tabella.
					// Ogni client ha la propria tabella dinamica.

					String varName = new String(msg.getData(),
							0,
							msg.getDataLen());
					short cliIndex = (short)msg.getSubCmd();
					//theApp.plc.AddDynamicVar(varName, index);
					// preparo messaggio
					//System.currentTimeMillis() - t1 > 3000)
					int i = getIndexFromClient(clientID);
					theApp.clientWorkerList[i].dynTable.addElement(cliIndex, varName);

					if (!sendMsgToClient(new DataFrame(msg.getCmd(),
													   cliIndex,
													   varName.length(),
													   varName.getBytes())))
					{
						theApp.disconnectClient(clientID);
						bConnected = false;
					}
				}
				break;
			    case NetConst.DISCONNECT_REQ:
				{
					sendMsgToClient(new DataFrame(msg.getCmd(),
							   0,
							   0,
							   null));
					// il client si disconnette
					System.out.println("disconnect clientID: " + clientID);
					theApp.disconnectClient(clientID);
				}
				break;
		    	case NetConst.GETMONITORVARINDEX_REQ:
			    {
	    			String varName = new String(msg.getData(),
	    					0,
	    					msg.getDataLen());
					
					System.out.println("varname: "+varName);
					if (!sendMsgToClient(new DataFrame(msg.getCmd(),
													   idx,
													   varName.length(),
													   varName.getBytes())))

					{
						theApp.disconnectClient(clientID);
						bConnected = false;
					}
					idx++;
				}
				break;
				default:
			}
	    }
  	}
}

/**
* Server
*/
class SocketThrdServer extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final int MAXCLIENTS = 10;
	static boolean bInitOpenPageRunning = false;
	static public EventManager evntManager;
   	JLabel lblCounter = new JLabel();
   	JLabel lblNumOfClients = new JLabel();
   	JLabel lblInfoMsg = new JLabel("Ready");
   	int nClients = 0;
	public ClientWorkerInfo[] clientWorkerList = {new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  new ClientWorkerInfo(),
											  };
	Socket sock;
	ServerSocket server = null;

   	SocketThrdServer() {
		super("JavaServer");
		//Center the window
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	Dimension frameSize = this.getSize();

    	//this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		//setSize(200,50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	    JPanel p = new JPanel();
	    JLabel lbl1 = new JLabel("Number of clients: ");
	    lbl1.setFont(new Font("Arial", Font.PLAIN, 12));

		JLabel lbl2 = new JLabel("Counter: ");
		lbl2.setFont(new Font("Arial", Font.PLAIN, 12));

		lblInfoMsg.setFont(new Font("Arial", Font.PLAIN, 12));

		lblNumOfClients.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNumOfClients.setBackground(new Color(174, 178, 195));
	    lblNumOfClients.setForeground(Color.blue);
	    lblNumOfClients.setBorder(BorderFactory.createLoweredBevelBorder());
	    lblNumOfClients.setOpaque(true);
	    lblNumOfClients.setRequestFocusEnabled(false);

	    lblCounter.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCounter.setBackground(new Color(174, 178, 195));
	    lblCounter.setForeground(Color.blue);
	    lblCounter.setBorder(BorderFactory.createLoweredBevelBorder());
	    lblCounter.setOpaque(true);
	    lblCounter.setRequestFocusEnabled(false);
	    //lblNumOfClients.setHorizontalAlignment(SwingNetConst.CENTER);

		Container contentPane = getContentPane();

		p.setLayout(new GridLayout(3,2));
		p.add(lbl1);
		p.add(lblNumOfClients);
	    p.add(lbl2);
	    p.add(lblCounter);

	    contentPane.add("North", p);

	    JPanel panelMessage = new JPanel();

	    panelMessage.add("West",lblInfoMsg);
	    contentPane.add("South", panelMessage);

	    eventManager();
	    // attivo keepalive
	    KeepaliveWatch keepWatch = new KeepaliveWatch(this);
	    Thread t = new Thread(keepWatch);
		t.start();
	}

	/**
	* manda il messaggio a tutti i client collegati
	*/
	public void sendBroadcastMSG(DataFrame msg) {
		for (int c=0; c < 10; c++) {
			if (clientWorkerList[c].bRunning) {
				//System.out.println("send msg to clientID: "+clientWorkerList[c].clientID);
				if (!clientWorkerList[c].w.sendMsgToClient(msg)) {
					// chiudo il thread
					disconnectClient(clientWorkerList[c].clientID);
				}
			}
		}
	}

	/**
	* connette il client
	*/
	public synchronized boolean connectClient()
	{
		boolean bFullClientList = true;
		int clientID=0;
		ClientWorker w;

		System.out.println("ConnectClient...");
		for (int i=0; i < MAXCLIENTS; i++) {
			if (!clientWorkerList[i].bRunning) {
				// libero
				clientID = i;

				bFullClientList = false;
				w = new ClientWorker(sock, clientID, this);

				clientWorkerList[i] = new ClientWorkerInfo();
				clientWorkerList[i].clientID = clientID;
				clientWorkerList[i].w = w;
				clientWorkerList[i].bRunning = true;
				break;
			}
		}
		if (bFullClientList)
			// lista esaurita
			return false;

		//sock.setTcpNoDelay(true);
		// thread di ricezione richieste client
		Thread t = new Thread(clientWorkerList[nClients].w);
		t.start();

		// update n. clients connessi
		++nClients;
		String s = "";
		s = String.valueOf(nClients);
		lblNumOfClients.setText(s);
		//System.out.println("connectClient: " + s);
		return true;
	}

	/**
	* scollega il client
	*/
	public synchronized void disconnectClient(int clientID) {
		for (int c=0; c < MAXCLIENTS; c++) {
			if (clientWorkerList[c].clientID == clientID) {
				clientWorkerList[c].bRunning = false;
				// fermo il thread
				clientWorkerList[c].w.terminate();
				break;
			}
		}
		if (nClients > 0)
			nClients--;
		// update n. clients connessi
		String s = "";
		s = String.valueOf(nClients);

		lblNumOfClients.setText(s);
		System.out.println("Server disconnect clientID " + clientID);
	}

	/**
	* attende client.Genera 1 thread di lavoro x client.
	*/
	public void listenSocket() {
		try {
			server = new ServerSocket(NetConst.PORTNUM);
		} catch (IOException e) {
			System.out.println("Could not listen on port: "+ NetConst.PORTNUM);
			System.exit(-1);
		}

		while (true) {
			int clientID=0;

			try {
				lblInfoMsg.setText("Waiting for client connection...");
				sock = server.accept();

				// connette e starta thread di servizio
				if (!connectClient()) {
					sock.close();
					System.out.println("Sorry:Full client list.");
				}
			} catch (IOException e) {
			    System.out.println("Accept failed on port: " + NetConst.PORTNUM);
			    System.exit(-1);
			}
		}
  	}

  	public void finalize() {
		try {
	        server.close();
	    } catch (IOException e) {
	        System.out.println("Could not close socket");
	        System.exit(-1);
	    }
	}

	/**
	* KeepaliveWatch
	*/
	class KeepaliveWatch extends Thread {
		Thread executor;
		private SocketThrdServer theApp;

		KeepaliveWatch(SocketThrdServer theApp) {
			if (executor == null) {
				executor = new Thread(this);
				this.theApp = theApp;
				executor.start();
			}
		}

		public void run() {
			while (true) {
				Timings.sleep(1000);
				GregorianCalendar gg = new GregorianCalendar();
				String s = gg.get(Calendar.HOUR_OF_DAY) + ":" + gg.get(Calendar.MINUTE) + ":" + gg.get(Calendar.SECOND);
				theApp.sendBroadcastMSG(new DataFrame(NetConst.TIME_EVENT, 0, s.length(), s.getBytes()));
			}
		}
	}

	/**
	* gestore eventi.Un solo thread che effettua dispatch degli eventi ai client
	* opportuni.
	*/
	public void eventManager() {
		evntManager = new EventManager(this);

		/*
		TrendEvent trend = new TrendEvent(this);
		Thread t1 = new Thread(trend);
		t1.start();*/
	}

	public static void main(String[] args) {
		SocketThrdServer frm = new SocketThrdServer();
		frm.setTitle("Java Server");

        WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//evntManager.terminate();
				System.exit(0);
			}
        };

        frm.addWindowListener(l);
        frm.pack();
        frm.setVisible(true);

        frm.listenSocket();
  }
}
