package cn.weisiqi;

//查看本机上所有的串口
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;

/**
 * 
 * @author 魏思琪
 * 
 */
public class ViewPorts {
	public void listPorts() {
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portId;
		while (enumeration.hasMoreElements()) {
			portId = (CommPortIdentifier) enumeration.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println("port name :" + portId.getName());
			}
		}
	}

	public static void main(String[] args) {
		ViewPorts vPorts = new ViewPorts();
		vPorts.listPorts();
	}
}