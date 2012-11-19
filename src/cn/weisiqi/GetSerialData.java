package cn.weisiqi;

//以添加监听事件方式获取串口数据
import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

/**
 * 
 * @author 魏思琪
 * 
 */
public class GetSerialData {
	public void listPort() {
		try {
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier("COM1");
			SerialPort Sport = (SerialPort) portId.open("test", 2000);
			System.out.println("串口 " + Sport.getName() + " 连接成功");
			final SerialPort sp = Sport;
			Sport.setSerialPortParams(2400, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
			@SuppressWarnings("unused")
			OutputStream os = Sport.getOutputStream();
			Sport.notifyOnDataAvailable(true);
			Sport.notifyOnBreakInterrupt(true);
			Sport.enableReceiveTimeout(500);
			Sport.addEventListener(new SerialPortEventListener() {
				public void serialEvent(SerialPortEvent e) {
					InputStream is = null;
					StringBuffer msgBuffer = new StringBuffer();
					try {
						is = sp.getInputStream();
					} catch (Exception h) {
						h.printStackTrace();
					}
					int newData = 0;
					switch (e.getEventType()) {
					case SerialPortEvent.DATA_AVAILABLE:
						while (newData != -1) {
							try {
								newData = is.read();
								if (newData == -1) {
									break;
								}
								if ('\r' == (char) newData) {
								} else {
									msgBuffer.append((char) newData);
								}
							} catch (Exception f) {
								f.printStackTrace();
							}
						}
						try {
							System.out.println("Result is:"
									+ Double.valueOf(msgBuffer.toString()));
						} catch (Exception b) {
							b.printStackTrace();
						} finally {
							try {
								is.close();
								sp.close();
							} catch (Exception c) {
								c.printStackTrace();
							}
						}
						break;
					case SerialPortEvent.BI:
						System.out.println("break recieve");
						break;
					default:
						break;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GetSerialData t = new GetSerialData();
		t.listPort();
	}
}