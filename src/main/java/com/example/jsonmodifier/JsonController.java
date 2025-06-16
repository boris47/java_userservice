package com.example.jsonmodifier;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.web.bind.annotation.*; // RestController RequestMapping RequestParam PostMapping

@RestController
@RequestMapping("/api")
public class JsonController
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
	
    @PostMapping("/modify-json")
    public JsonNode modifyJson(@RequestParam("file") org.springframework.web.multipart.MultipartFile file)
	{
		ObjectNode outValue = null;
		
		try
		{
			outValue = (ObjectNode) objectMapper.readTree(file.getInputStream());
		}
		catch (Exception e)
		{
			System.err.println(e);
			
			outValue = new ObjectNode(null);
		}
		
		if (outValue != null)
		{
			if (outValue.has("age"))
			{
				JsonNode ageNode = outValue.get("age");
				
				int age = 0;
				if (ageNode.isInt())
				{
					age = ageNode.asInt();
				}
				else if (ageNode.isTextual())
				{
					try
					{
						age = Integer.parseInt(ageNode.asText());
					}
					catch (Exception ex) {}
				}
				
				outValue.put("age", age + 1);
			}
			
			// overwrite name
			outValue.set("name", TextNode.valueOf("Aldo"));
			
			System.out.println(outValue.toPrettyString());
		}
		
		return outValue;
    }
}
