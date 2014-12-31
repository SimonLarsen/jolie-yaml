package io.github.simonlarsen.yaml;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;
import jolie.runtime.FaultException;

import org.yaml.snakeyaml.Yaml;

public class YamlUtils extends JavaService {
	private void parse(Object doc, Value value) {
		if(doc instanceof List) {
			for(Object o : (List)doc) {
				parse(o, value.getNewChild("seq"));
			}
		}
		else if(doc instanceof Map) {
			Map<String,Object> map = (Map<String,Object>)doc;

			for(Map.Entry<String,Object> entry : map.entrySet()) {
				parse(entry.getValue(), value.getFirstChild(entry.getKey()));
			}
		}
		else {
			value.setValue(doc);
		}
	}

	public Value parseYamlFile(Value request) throws FaultException {
		try {
			String filename = request.strValue();

			InputStream is = new FileInputStream(new File(filename));

			Yaml yaml = new Yaml();
			Object doc = yaml.load(is);

			Value retValue = Value.create();
			parse(doc, retValue);

			return retValue;
		} catch(FileNotFoundException fnfe) {
			throw new FaultException("FileNotFound");
		}
	}
}
