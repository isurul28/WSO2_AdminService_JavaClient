import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.inbound.stub.InboundAdminInboundManagementException;
import org.wso2.carbon.inbound.stub.InboundAdminStub;
import org.wso2.carbon.inbound.stub.types.carbon.InboundEndpointDTO;
import org.wso2.carbon.service.mgt.stub.ServiceAdminException;
import org.wso2.carbon.service.mgt.stub.ServiceAdminStub;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaDataWrapper;

public class InboundAdminServiceClient {

	private final String serviceName = "InboundAdmin";
    private InboundAdminStub inboundAdminStub;
    private String endPoint;
    private ServiceAdminStub serviceAdminStub;

    public InboundAdminServiceClient(String backEndUrl, String sessionCookie) throws AxisFault {
        
    	this.endPoint = backEndUrl + "/services/" + serviceName;
        inboundAdminStub = new InboundAdminStub(endPoint);
        //Authenticate Your stub from sessionCooke
        ServiceClient serviceClient;
        Options option;

        serviceClient = inboundAdminStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
    }

    public void enableOrDisableInboundEndpoint(String endpointName,String suspend) throws RemoteException, InboundAdminInboundManagementException {
    	/*inboundAdminStub.removeInboundEndpoint(endpointName);*/
    	InboundEndpointDTO inboundEndpointDTO= inboundAdminStub.getInboundEndpointbyName(endpointName);
    	inboundAdminStub.updateInboundEndpoint(inboundEndpointDTO.getName(), inboundEndpointDTO.getInjectingSeq(), inboundEndpointDTO.getOnErrorSeq(), inboundEndpointDTO.getProtocol(), inboundEndpointDTO.getClassImpl(), suspend, inboundEndpointDTO.getParameters());
    	System.out.println(inboundAdminStub.getInboundEndpointbyName(endpointName).isSuspendSpecified());
    	
    	/*InboundEndpointDTO[] endpointDTO = inboundAdminStub.getAllInboundEndpointNames();
    	for (InboundEndpointDTO inboundEndpointDTO2 : endpointDTO) {
			System.out.println(inboundEndpointDTO2.getArtifactContainerName());
		}*/
    	
    }
    public String getInboundName () {
    	
    	return serviceName;
    }

    public ServiceMetaDataWrapper listServices() throws RemoteException {
        return serviceAdminStub.listServices("ALL", "*", 0);
    }

    public void disableService(String serviceName) throws RemoteException, ServiceAdminException {
        serviceAdminStub.stopService(serviceName);
    }
    public void changeServiceState(String serviceName, boolean isActive) throws RemoteException, ServiceAdminException {
        serviceAdminStub.changeServiceState(serviceName, isActive);
    }
    public void enableService(String serviceName) throws RemoteException, ServiceAdminException {
        serviceAdminStub.startService(serviceName);
    }

    public boolean inboundEndpointStatus(String endpointName) throws RemoteException, InboundAdminInboundManagementException{
    	return inboundAdminStub.getInboundEndpointbyName(endpointName).isSuspendSpecified();
    }

    
    public ServiceMetaData getServiceDetails(String serviceName) throws RemoteException, ServiceAdminException {
        return serviceAdminStub.getServiceData(serviceName);
    }
}
