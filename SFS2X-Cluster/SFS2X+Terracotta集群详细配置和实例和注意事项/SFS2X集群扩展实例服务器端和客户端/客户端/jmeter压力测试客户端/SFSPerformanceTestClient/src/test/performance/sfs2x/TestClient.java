package test.performance.sfs2x;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.User;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.PublicMessageRequest;

/**
 * ʹ��jmeter����sfs2x�������˵�����
 * 
 * ��������
 * @author �½�
 *
 */
public class TestClient extends AbstractJavaSamplerClient implements IEventListener{
	
	private SmartFox smartFox = null;
	
	private SampleResult testResult;
	
	private boolean completed = false;
	
	private User currentUser = null;
	
	@Override
	public void setupTest(JavaSamplerContext context){
		
		smartFox = new SmartFox(true);
		smartFox.addEventListener(SFSEvent.CONNECTION, this);
		smartFox.addEventListener(SFSEvent.CONNECTION_LOST, this);
		smartFox.addEventListener(SFSEvent.LOGIN, this);
		smartFox.addEventListener(SFSEvent.ROOM_JOIN, this);
		smartFox.addEventListener(SFSEvent.LOGIN_ERROR, this);
		
		smartFox.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent event) throws SFSException {
				if("userJoined".equals(event.getArguments().get("cmd"))){
					ISFSObject paramsRes = (ISFSObject)event.getArguments().get("params");
					
					String username = paramsRes.getUtfString("username");
					
					System.out.println(String.format("user %s entered", username));
				}else if("userLeft".equals(event.getArguments().get("cmd"))){
					ISFSObject paramsRes = (ISFSObject)event.getArguments().get("params");
					String username = paramsRes.getUtfString("username");
					
					System.out.println(String.format("user %s left", username));
				}else if("clusterPublicMessage".equals(event.getArguments().get("cmd"))){
					ISFSObject paramsRes = (ISFSObject)event.getArguments().get("params");
					String username = paramsRes.getUtfString("username");
					String msg = paramsRes.getUtfString("message");
					
					System.out.println(String.format("%s ˵ %s", username, msg));
				}
			}
		});	
		
		testResult = new SampleResult();
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		testResult.sampleStart(); //������ʼ
		smartFox.loadConfig(true);
		
		while(!completed){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				completed = true;
				testResult.setSuccessful(false);
				e.printStackTrace();
			}
		}
		
		testResult.sampleEnd();
		
		return testResult;
	}
	
	//��ȡIP���һ����
	public String getClientIp() throws UnknownHostException{
		
		String wholeIp = InetAddress.getLocalHost().getHostAddress();
		
		return wholeIp.substring(wholeIp.lastIndexOf(".")+1);
	}
	
	@Override
	public void dispatch(BaseEvent event) throws SFSException {
		String type = event.getType();
		if(SFSEvent.CONNECTION.equalsIgnoreCase(type)){

			if(event.getArguments().get("success").equals(true)){
				
				
				try {
					String name = getClientIp()+"#"+Thread.currentThread().getId();
					smartFox.send(new LoginRequest(name));
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}else{
				System.out.println("connection fail");
			}
			
			//
		}else if(SFSEvent.CONNECTION_LOST.equalsIgnoreCase(type)){
			testResult.setSuccessful(true);
			completed = true;
		}
		else if(SFSEvent.LOGIN.equalsIgnoreCase(type)){
			System.out.println("login success,join the room");
			
			currentUser = (User)event.getArguments().get("user");
			
			
			
			smartFox.send(new JoinRoomRequest("demo"));
			//smartFox.send(new ExtensionRequest("getTime", new SFSObject(),smartFox.getLastJoinedRoom()));
			
		}else if(SFSEvent.ROOM_JOIN.equalsIgnoreCase(type)){
			System.out.println("room joined success, begin sending message");
			
			System.out.println(String.format("%s ��ʼ������Ϣ", currentUser.getName()));

			smartFox.send(new PublicMessageRequest(String.format("%s ����Ŷ,��һ�ӭ��?", currentUser.getName())));
		}
		
		else if(SFSEvent.LOGIN_ERROR.equalsIgnoreCase(type)){
			
			System.out.println("login err");
			
			testResult.setSuccessful(false);
			completed = true;
			
		}else if(SFSEvent.LOGOUT.equalsIgnoreCase(type)){
			testResult.setSuccessful(true);
			completed = true;
			System.out.println("log out");
			
		}
		
	}

}
