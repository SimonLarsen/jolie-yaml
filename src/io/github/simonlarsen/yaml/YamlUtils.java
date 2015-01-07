package io.github.simonlarsen.yaml;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;
import jolie.runtime.FaultException;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.ComposerException;

public class YamlUtils extends JavaService {
	private void parse(Object doc, Value value) {
		if(doc instanceof List) {
			for(Object o : (List)doc) {
				parse(o, value.getNewChild("list"));
			}
		}
		else if(doc instanceof Map) {
			Map<String,Object> map = (Map<String,Object>)doc;

			for(Map.Entry<String,Object> entry : map.entrySet()) {
				parse(entry.getValue(), value.getFirstChild(entry.getKey()));
			}
		}
		else if(doc instanceof Set) {
			for(Object o : (Set<Object>)doc) {
				parse(o, value.getNewChild("set"));
			}
		}
		else {
			value.setValue(doc);
		}
	}

	public Value parse(Value request) throws FaultException {
		try {
			String filename = request.strValue();
			InputStream is = new FileInputStream(new File(filename));

			Yaml yaml = new Yaml();
			Object doc = yaml.load(is);

			Value value = Value.create();
			parse(doc, value);

			return value;
		} catch(FileNotFoundException fnfe) {
			throw new FaultException("FileNotFound");
		} catch(ComposerException ce) {
			throw new FaultException("MultipleDocuments");
		}
	}

	public Value parseAll(Value request) throws FaultException {
		try {
			String filename = request.strValue();
			InputStream is = new FileInputStream(new File(filename));

			Yaml yaml = new Yaml();
			Value value = Value.create();

			for(Object doc : yaml.loadAll(is)) {
				parse(doc, value.getNewChild("doc"));
			}

			return value;
		} catch(FileNotFoundException fnfe) {
			throw new FaultException("FileNotFound");
		}
	}
}
