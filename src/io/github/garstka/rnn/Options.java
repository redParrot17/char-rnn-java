package io.github.garstka.rnn;

import java.io.*;
import java.util.Properties;
import io.github.garstka.rnn.math.Math;

/**
 * Application options.
 */
public class Options {

	/* Model parameters */

	private int hiddenSize; // Size of a single RNN layer hidden state.
	static final int hiddenSizeDefault = 100;

	private int layers; // How many layers in a net?
	static final int layersDefault = 2;

	/* Training parameters */

	private int sequenceLength; // How many steps to unroll during training?
	static final int sequenceLengthDefault = 50;

	private double learningRate; // The network learning rate.
	static final double learningRateDefault = 0.1;

	/* Sampling parameters */

	// Sampling temperature (0.0, 1.0]. Lower
	// temperature means more conservative
	// predictions.
	private double samplingTemp;
	static final double samplingTempDefault = 1.0;

	/* Other options */

	private boolean printOptions; // Print options at the start.
	static final boolean printOptionsDefault = true;

	private int trainingSampleLength; // Length of a sample during training.
	static final int trainingSampleLengthDefault = 400;

	private int
	    snapshotEveryNSamples; // Take a network's snapshot every N samples.
	static final int snapshotEveryNSamplesDefault = 50;

	private int
	    loopAroundTimes; // Loop around the training data this many times.
	static final int loopAroundTimesDefault = 0;

	private int
	    sampleEveryNSteps; // Take a sample during training every N steps.
	static final int sampleEveryNStepsDefault = 100;

	private String inputFile; // The training data.
	static final String inputFileDefault = "input.txt";

	private boolean useSingleLayerNet; // Use the simple, single layer net.
	static final boolean useSingleLayerNetDefault = false;


	/* Load */

	private Properties prop;

	/**
	 * Constructs, uses the defaults.
	 */
	Options() {
		prop = new Properties();
		setDefaults();
	}

	/**
	 * Loads the options from the file, or uses defaults if not found.
	 */
	Options(String file) throws IOException {
		this();
		try (InputStream in = new FileInputStream(file)) {
			prop.load(in);
			getProperties();
		} catch (IOException e) {
			throw new IOException(
			    "Loading config from " + file + " failed.", e);
		}
	}

	/* Print */

	/**
	 * Prints the options.
	 */
	void print() {
		setProperties();
		prop.list(System.out);
	}

	/* Save */

	/**
	 * Saves to file.
	 */
	void save(String file) throws IOException {
		setProperties();
		try (OutputStream out = new FileOutputStream(file)) {
			prop.store(out, "---RNN properties---");
		} catch (IOException e) {
			throw new IOException("Saving config to " + file + " failed.", e);
		}
	}

	/* Get */

	int getHiddenSize() {
		return hiddenSize;
	}

	int getLayers() {
		return layers;
	}

	int getSequenceLength() {
		return sequenceLength;
	}

	double getLearningRate() {
		return learningRate;
	}

	double getSamplingTemp() {
		return samplingTemp;
	}

	boolean getPrintOptions() {
		return printOptions;
	}

	String getInputFile() {
		return inputFile;
	}

	boolean getUseSingleLayerNet() {
		return useSingleLayerNet;
	}

	int getTrainingSampleLength() {
		return trainingSampleLength;
	}

	int getLoopAroundTimes() {
		return loopAroundTimes;
	}

	int getSampleEveryNSteps() {
		return sampleEveryNSteps;
	}

	int getSnapshotEveryNSamples() {
		return snapshotEveryNSamples;
	}

	/* Helper */

	/**
	 * Sets the default values.
	 */
	private void setDefaults() {
		hiddenSize = hiddenSizeDefault;
		layers = layersDefault;

		sequenceLength = sequenceLengthDefault;
		learningRate = learningRateDefault;

		samplingTemp = samplingTempDefault;

		printOptions = printOptionsDefault;
		trainingSampleLength = trainingSampleLengthDefault;
		loopAroundTimes = loopAroundTimesDefault;
		sampleEveryNSteps = sampleEveryNStepsDefault;
		snapshotEveryNSamples = snapshotEveryNSamplesDefault;
		inputFile = inputFileDefault;
		useSingleLayerNet = useSingleLayerNetDefault;
	}

	/**
	 * Validates the properties and sets to default values where failed.
	 */
	private void validateProperties() {
		validateHiddenSize();
		validateLayers();
		validateSequenceLength();
		validateLoopAroundTimes();
		validateSampleEveryNSteps();
		validateSnapshotEveryNSamples();
		validateLearningRate();
		validateSamplingTemp();
		validateTrainingSampleLength();
	}

	private void validateHiddenSize() {
		if (hiddenSize < 1) {
			hiddenSize = hiddenSizeDefault;
			System.out.println("Hidden size must be >= 1. Using default " + hiddenSize + ".");
		}
	}

	private void validateLayers() {
		if (layers < 1) {
			layers = layersDefault;
			System.out.println("Layer count must be >= 1. Using default " + layers + ".");
		}
	}

	private void validateSequenceLength() {
		if (sequenceLength < 1) {
			sequenceLength = sequenceLengthDefault;
			System.out.println("Sequence length must be >= 1. Using default " + sequenceLength + ".");
		}
	}

	private void validateLoopAroundTimes() {
		if (loopAroundTimes < 0) {
			loopAroundTimes = loopAroundTimesDefault;
			System.out.println("Loop around times must be >= 0. Using default " + loopAroundTimes + ".");
		}
	}

	private void validateSampleEveryNSteps() {
		if (sampleEveryNSteps < 1) {
			sampleEveryNSteps = sampleEveryNStepsDefault;
			System.out.println("Sample every N steps: N must be >= 1. Using default " + sampleEveryNSteps + ".");
		}
	}

	private void validateSnapshotEveryNSamples() {
		if (snapshotEveryNSamples < 1) {
			snapshotEveryNSamples = snapshotEveryNSamplesDefault;
			System.out.println("Snapshot every N samples: N must be >= 1. Using default " + snapshotEveryNSamples + ".");
		}
	}

	private void validateLearningRate() {
		if (learningRate < 0.0) {
			learningRate = learningRateDefault;
			System.out.println("Learning rate must be >= 0. Using default " + learningRate + ".");
		}
	}

	private void validateSamplingTemp() {
		if (Math.close(samplingTemp, 0.0) || samplingTemp < 0.0 || samplingTemp > 1.0 + Math.eps()) {
			learningRate = learningRateDefault;
			System.out.println("Learning rate must be in (0.0,1.0]. Using default " + learningRate + ".");
		}
	}

	private void validateTrainingSampleLength() {
		if (trainingSampleLength < 1) {
			trainingSampleLength = trainingSampleLengthDefault;
			System.out.println("Training sample length must be >= 1. Using default " + trainingSampleLength + ".");
		}
	}

	/**
	 * Gets the properties from the Properties class.
	 */
	private void getProperties() {
		hiddenSize = parseInt("hiddenSize", hiddenSizeDefault);
		layers = parseInt("layers", layersDefault);
		sequenceLength = parseInt("sequenceLength", sequenceLengthDefault);
		learningRate = parseDouble("learningRate", learningRateDefault);
		samplingTemp = parseDouble("samplingTemp", samplingTempDefault);
		printOptions = parseBool("printOptions", printOptionsDefault);
		trainingSampleLength = parseInt("trainingSampleLength", trainingSampleLengthDefault);
		loopAroundTimes = parseInt("loopAroundTimes", loopAroundTimesDefault);
		sampleEveryNSteps = parseInt("sampleEveryNSteps", sampleEveryNStepsDefault);
		snapshotEveryNSamples = parseInt("snapshotEveryNSamples", snapshotEveryNSamplesDefault);
		inputFile = prop.getProperty("inputFile");
		useSingleLayerNet = parseBool("useSingleLayerNet", useSingleLayerNetDefault);

		validateProperties();
	}

	/**
	 * Saves the properties in the Properties class.
	 */
	private void setProperties() {
		prop.setProperty("hiddenSize", Integer.toString(hiddenSize));
		prop.setProperty("layers", Integer.toString(layers));
		prop.setProperty("sequenceLength", Integer.toString(sequenceLength));
		prop.setProperty("learningRate", Double.toString(learningRate));
		prop.setProperty("samplingTemp", Double.toString(samplingTemp));
		prop.setProperty("printOptions", Boolean.toString(printOptions));
		prop.setProperty("trainingSampleLength", Integer.toString(trainingSampleLength));
		prop.setProperty("loopAroundTimes", Integer.toString(loopAroundTimes));
		prop.setProperty("sampleEveryNSteps", Integer.toString(sampleEveryNSteps));
		prop.setProperty("snapshotEveryNSamples", Integer.toString(snapshotEveryNSamples));
		prop.setProperty("inputFile", inputFile);
		prop.setProperty("useSingleLayerNet", Boolean.toString(useSingleLayerNet));
	}

	/**
	 * Parses int, returns the default value if failed.
	 */
	private int parseInt(String name, int defaultValue) {
		try {
			return Integer.parseInt(prop.getProperty(name));
		} catch (NumberFormatException e) {
			System.out.println("Error parsing " + name + ": "
			    + prop.getProperty(name) + ", defaulting to: "
			    + defaultValue);
			return defaultValue;
		}
	}

	/**
	 * Parses double, returns the default value if failed.
	 */
	private double parseDouble(String name, double defaultValue) {
		try {
			return Double.parseDouble(prop.getProperty(name));
		} catch (NumberFormatException e) {
			System.out.println("Error parsing " + name + ": "
			    + prop.getProperty(name) + ", defaulting to: "
			    + defaultValue);
			return defaultValue;
		}
	}

	/**
	 * Parses boolean, returns the default value if failed.
	 */
	private boolean parseBool(String name, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(prop.getProperty(name));
		} catch (NumberFormatException e) {
			System.out.println("Error parsing " + name + ": "
			    + prop.getProperty(name) + ", defaulting to: "
			    + defaultValue);
			return defaultValue;
		}
	}
}