package demo.sfs2x.client;

import java.util.List;

public class SFSNodeUtils {
	
	/**
	 * ���ص�ǰ�û������ٵĽڵ�
	 * @param nodes
	 * @return
	 */
	public static SFSNode getMiniLoadedNode(List<SFSNode> nodes){
		
		if(nodes == null || nodes.size() == 0)return null;
		
		SFSNode min = nodes.get(0);
		
		for(int i=0; i<nodes.size(); i++){
			SFSNode node = nodes.get(i);
			
			if(min.getCurrentUsers()>node.getCurrentUsers()){
				min = node;
			}
			
		}
		
		return min;
	}

}
