package cn.weisiqi;

import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

/**
 * 
 * @author 魏思琪
 * 
 */
// 设置缓冲屈来读取串口数据
// 串口操作主类
public class SerialBean {
	static String portName;
	CommPortIdentifier portId;
	SerialPort serialPort;
	static InputStream in;
	static OutputStream out;
	SerialBuffer sb;
	ReadSeriral rt;

	public SerialBean(int portId) {
		portName = "COM" + String.valueOf(portId);
	}

	public int Initialize() {
		int initsuccess = 1;
		int initfail = -1;
		try {
			portId = CommPortIdentifier.getPortIdentifier(portName);
			try {
				serialPort = (SerialPort) portId.open("serial_communication",
						2000);
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			} catch (Exception e) {
				System.out.println("初始化串口失败！");
				return initfail;
			}
			sb = new SerialBuffer();
			rt = new ReadSeriral(sb, in);
			rt.start();
			return initsuccess;
		} catch (Exception e) {
			System.out.print("初始化串口失败!");
			return initfail;
		}
	}

	public String ReadPort(int length) {
		String msg = "";
		msg = sb.getMsg(length);
		return msg;
	}

	public void WritePort(String msg) {
		try {
			for (int i = 0; i < msg.length(); i++) {
				out.write(msg.charAt(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void ClosePort() {
		try {
			rt.stop();
			serialPort.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPortName() {
		return portName;
	}

	public static void main(String[] args) {
		SerialBean sb = new SerialBean(1);
		String msg;
		sb.Initialize();
		System.out.println("串口" + sb.getPortName() + "初始化成功！");
		for (int i = 5; i <= 10; i++) {
			msg = sb.ReadPort(i);
			sb.WritePort("reply:" + msg);
		}
		sb.ClosePort();
	}
}

class SerialBuffer {
	private String contentString = "";
	private String currentMsg, tempContent;
	private boolean available = false;
	private int lengthNeeded = 1;

	public synchronized String getMsg(int length) {
		lengthNeeded = length;
		notifyAll();
		if (lengthNeeded > contentString.length()) {
			available = false;
			while (available == false) {
				try {
					wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		currentMsg = contentString.substring(0, lengthNeeded);
		tempContent = contentString.substring(lengthNeeded);
		contentString = tempContent;
		lengthNeeded = 1;
		notifyAll();
		return currentMsg;
	}

	public synchronized void putChar(int c) {
		Character d = new Character((char) c);
		contentString = contentString.concat(d.toString());
		if (lengthNeeded < contentString.length()) {
			available = true;
		}
		notifyAll();
	}
}

class ReadSeriral extends Thread {
	private SerialBuffer commBuffer;
	private InputStream commPort;

	public ReadSeriral(SerialBuffer sb, InputStream port) {
		commBuffer = sb;
		commPort = port;
	}

	public void run() {
		int c;
		try {
			while (true) {
				c = commPort.read();
				commBuffer.putChar(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}