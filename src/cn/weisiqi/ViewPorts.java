package cn.weisiqi;

//�鿴���������еĴ���
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;

/**
 * 
 * @author κ˼��
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