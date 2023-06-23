//created by Isurul

import java.util.ArrayList;

import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.inbound.stub.InboundAdminInboundManagementException;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaDataWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;   

public class Main {
    public static void main(String[] args)
            throws LoginAuthenticationExceptionException,
            LogoutAuthenticationExceptionException, IOException, InboundAdminInboundManagementException {
    	
		
		  String TrustStroepath = args[0]; String TrustStroepass = args[1]; String
		  Username = args[2]; String Password = args[3];
		  System.setProperty("javax.net.ssl.trustStore", TrustStroepath);
		  System.setProperty("javax.net.ssl.trustStorePassword", TrustStroepass);
		 
		
		/*
		 * System.setProperty("javax.net.ssl.trustStore",
		 * "/home/isurul/Documents/test/Ticket/test/wso2ei-6.6.0/repository/resources/security/wso2carbon.jks"
		 * ); System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		 */
		 
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = "https://localhost:9443";
        LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);

        String session = login.authenticate(Username, Password);
        //String session = login.authenticate("admin", "admin");

        ServiceAdminClient serviceAdminClient = new ServiceAdminClient(backEndUrl, session);
        ServiceMetaDataWrapper serviceList = serviceAdminClient.listServices();
        System.out.println("Service List:");
        FileWriter myWriter = new FileWriter("servicelist.txt");
        
      
        for (ServiceMetaData serviceData : serviceList.getServices()) {
           
            
            System.out.println("****************************************************");
            myWriter.write("ServiceName:"+ serviceData.getName()+" ");
            

            System.out.println("service Name:"+ serviceData.getName());
            myWriter.write("ServiceStatus:"+ serviceData.getActive());

            System.out.println("Service Status:"+ serviceData.getActive());
            myWriter.append("\n");
            System.out.println("****************************************************");
           

        }
        
        //Checking status of inbound-endpoint
        
        BufferedReader bufReader = new BufferedReader(new FileReader("inbound.txt")); 
        ArrayList<String> listOfLines = new ArrayList<>(); 
        String line = bufReader.readLine(); 
        while (line != null) 
        { 
        	listOfLines.add(line); line = bufReader.readLine(); 
        } 
        bufReader.close();
                
        for (String name : listOfLines) { 		      
        	InboundAdminServiceClient endpointServiceClient = new InboundAdminServiceClient(backEndUrl, session);
    		boolean status = endpointServiceClient.inboundEndpointStatus(name);
            //System.out.println("****************************************************");
            myWriter.write("ServiceName:"+"ID_"+ name+" ");
            myWriter.write("ServiceStatus:"+ status);
            myWriter.append("\n");
    		//System.out.println(status);
    		
            //System.out.println("****************************************************");	
        }
        myWriter.close();

		


      

        login.logOut();
    }
}
