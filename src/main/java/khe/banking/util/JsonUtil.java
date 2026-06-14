package khe.banking.util;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;

public class JsonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private JsonUtil() {		
	}
	
	public static String toJson(Object obj) {
		if(obj == null) {
			return null;
		}
		
		try {
			return mapper.writeValueAsString(obj);
		} catch(JsonNodeException e) {
			throw new RuntimeException("Failed to serialize object", e);
		}
	}
	
}
