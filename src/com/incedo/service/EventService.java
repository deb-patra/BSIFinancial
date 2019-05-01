/**
 * 
 */
package com.incedo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.incedo.vos.EventSubmitRequestVO;
import com.incedo.vos.ExperimentVariantVo;


/**
 * @author Deb
 *
 */
public class EventService {

	public ExperimentVariantVo getEventJsonFromServiceAPI(String userId) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream input = classLoader.getResourceAsStream("/com/incedo/resource/configuration.properties");
    	Properties prop = new Properties();
    	prop.load(input);
        String getserviceURL = prop.getProperty("service.api.url");
    	int layerId = Integer.parseInt(prop.getProperty("layer.id"));
    	int channelId = Integer.parseInt(prop.getProperty("channel.id"));
		URL url;
		String jsonString = null;
		String apiUrl = getserviceURL +"?channel_id="+channelId+"&layer_id="+layerId+"&user_id="+userId;
		System.out.println("apiUrl ::"+apiUrl);
		try {
			url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				jsonString = output;
			}
			conn.disconnect();
		} catch (IOException e) {
			System.out.println("IO Exception : "+e.getMessage());
		}	
		String variantToken = null;
		String exptName = null;
		String bucket = null;
		String layerName = null;
		String channelName = null;
		int expId = 1;
		int variantId = 0;
		JSONObject obj = new JSONObject(jsonString);
		if(obj.has("variant_token")) {
			variantToken = obj.get("variant_token").toString();
		}
		if(obj.has("bucket")) {
			bucket = obj.get("bucket").toString();
		}
		if(obj.has("exp_id")) {
			expId = (Integer) obj.get("exp_id");
		}
		if(obj.has("variant_id")) {
			variantId = (Integer) obj.get("variant_id");
		}
		if(obj.has("expt_name")) {
			exptName = (String) obj.get("expt_name");
		}
		if(obj.has("layer_name")) {
			layerName = (String) obj.get("layer_name");
		}
		if(obj.has("channel_name")) {
			channelName = (String) obj.get("channel_name");
		}
		ExperimentVariantVo experimentVariantVo = new ExperimentVariantVo();
		experimentVariantVo.setBucket(bucket);
		experimentVariantVo.setVariantToken(variantToken);
		experimentVariantVo.setExpId(expId);
		experimentVariantVo.setVariantId(variantId);
		experimentVariantVo.setExptName(exptName);
		experimentVariantVo.setLayerName(layerName);
		experimentVariantVo.setChannelName(channelName);
		experimentVariantVo.setUserId(userId);
		experimentVariantVo.setLayerId(layerId);
		experimentVariantVo.setChannelId(channelId);
		return experimentVariantVo;
	}
	
	public void pushNewEvent(EventSubmitRequestVO eventSubmit ) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    	InputStream input = classLoader.getResourceAsStream("/com/incedo/resource/configuration.properties");
	    	Properties prop = new Properties();
	    	prop.load(input);
			String requestJSON = mapper.writeValueAsString(eventSubmit);
			System.out.println("requestJSON ::"+requestJSON);
			String postEventserviceApi = prop.getProperty("postevent.api.url");
			System.out.println("postEventserviceApi ::"+postEventserviceApi);
			URL url = new URL(postEventserviceApi);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(requestJSON.getBytes());
			os.flush();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public EventSubmitRequestVO incedoEvent(ExperimentVariantVo experimentVariantVo, String stage) {
		UUID uuid = Generators.timeBasedGenerator().generate();
		EventSubmitRequestVO eventSubmit = new EventSubmitRequestVO();
		eventSubmit.setUser_id(experimentVariantVo.getUserId());
		eventSubmit.setEvt_id(uuid.toString());
		eventSubmit.setVariant_id(experimentVariantVo.getVariantId());
		int expId = experimentVariantVo.getExpId();
		String variantToken = experimentVariantVo.getVariantToken();
		if((variantToken.contains("control") || variantToken.contains("Control"))) {
			expId = -1;
		} 
		eventSubmit.setExp_id(expId);
		eventSubmit.setLayer_id(experimentVariantVo.getLayerId());
		eventSubmit.setChannel_id(experimentVariantVo.getChannelId());
		eventSubmit.setStage(stage);
		Instant instant = Instant.now();
		Long timeStampSeconds = instant.getEpochSecond();
		if(null != timeStampSeconds) {
			eventSubmit.setTime(timeStampSeconds.intValue());
		}
		System.out.println("eventSubmit:::"+eventSubmit);
		return eventSubmit;
	}
	
	public String incedoGetVariantToken(ExperimentVariantVo experimentVariantVo) {
		return experimentVariantVo.getVariantToken();
	}
	
	public String getStage(String pageName) {
    	String stageName = "checkoutStage";
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream input = classLoader.getResourceAsStream("/com/incedo/resource/configuration.properties");
    	Properties prop = new Properties();
    	try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(pageName.equalsIgnoreCase("promo")) {
    		stageName = prop.getProperty("promoStage");
    	} 
		return stageName;
    }
}
