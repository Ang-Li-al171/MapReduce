package postprocess;

import keyvaluepair.KeyValuePair;

public interface PostProcess {

	public void receiveKVP(KeyValuePair<String, Integer> kvp);

}
