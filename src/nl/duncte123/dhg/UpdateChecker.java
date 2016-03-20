package nl.duncte123.dhg;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker {
	
	
	private Dhg plugin;
	private URL filesFeed;
	
	private String version;
	private String link;
	
	public UpdateChecker(Dhg plugin, String url){
		this.plugin = plugin;
		
		
		try{
			filesFeed = new URL(url);
		}catch(MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean updateNeeded(){
		try{
			InputStream input = filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			
			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList childeren = latestFile.getChildNodes();
			
			version = childeren.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
			link = childeren.item(3).getTextContent();
			
			if(!(plugin.getDescription().getVersion().equals(version))){
					return true;
				
			}
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public String getVersion(){
		return version;
	}
	public String getLink(){
		return link;
	}

}
