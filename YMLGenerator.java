import java.util.*;
import com.beust.jcommander.*;

/*
 * Code to generate YAML file based on command line parameters passed to code
 * YAML file will be used as Kubernetes input feed 
 * 
 */

class YMLGenerator {
	@Parameter
	private List<String> parameters = new ArrayList<>();

	@Parameter(names = { "-Kind" }, description = "Kind of Object", required = true)
	private String kind = "Deployment";

	@Parameter(names = { "-Label" }, description = "Label")
	private String label = "Test";

	@Parameter(names = { "-Replica" }, description = "Number of Replicas")
	private Integer replica = 1;

	@Parameter(names = { "-HealthCheck" }, description = "Liveness and Readiness")
	private Integer health = 1;

	@Parameter(names = { "-Image" }, description = "Docker Image")
	private String image = "test";

	@Parameter(names = { "-ImageName" }, description = "Docker Image Name Tag")
	private String imageName = "";

	@Parameter(names = { "-w" }, description = "Working Directory")
	private String workDir = "./";

	@Parameter(names = "-v", description = "Volume Map")
	private List<String> vol = new ArrayList<>();

	@Parameter(names = "-e", description = "Environment")
	private List<String> env = new ArrayList<>();

	@Parameter(names = "-p", description = "Port")
	private List<String> ports = new ArrayList<>();

	@Parameter(names = "-tp", description = "Target Port")
	private String targetPorts = "";

	@Parameter(names = "-mem", description = "Memory")
	private String memory = "";

	@Parameter(names = "-cpu", description = "CPU")
	private String cpu = "";

	@Parameter(names = "-configmap", description = "Config Map file")
	private String configmap = "";

	@Parameter(names = "-secret", description = "Secret file")
	private String secret = "";

	@Parameter(names = { "-ExternalIP" }, description = "External IP")
	private String externalIP = "";

	@Parameter(names = { "-Selector" }, description = "Selector")
	private String selector = "";

	@Parameter(names = { "-Type" }, description = "Type")
	private String type = "";

	@Parameter(names = { "-nv" }, description = "Name-Value Pair")
	private List<String> nv = new ArrayList<>();

	static final String INDENT = "  ";
	static final String DBLINDENT = "    ";
	static final String TRPLINDENT = "      ";
	static final String QUADINDENT = "        ";
	static final String QUININDENT = "          ";
	static final String SEXINDENT = "             ";
	static final String SEPTINDENT = "              ";
	static final String apiVersionLbl = "apiVersion";
	static final String kindStr = "kind";
	static final String metadataStr = "metadata";
	static final String labelStr = "labels";
	static final String purposeStr = "purpose";
	static final String nameStr = "name";
	static final String specStr = "spec";
	static final String replicaStr = "replicas";
	static final String templateStr = "template";
	static final String appStr = "app";
	static final String containersStr = "containers";
	static final String envStr = "env";
	static final String envValStr = "value";
	static final String imageStr = "image";
	static final String workDirStr = "workingDir";
	static final String portsStr = "ports";
	static final String portStr = "port";
	static final String targetPortStr = "targetPort";
	static final String externalIPStr = "externalIPs";
	static final String containerPortStr = "containerPort";
	static final String volMountStr = "volumeMounts";
	static final String mountPathStr = "mountPath";
	static final String volumesStr = "volumes";
	static final String hostPathStr = "hostPath";
	static final String pathStr = "path";
	static final String selectorStr = "selector";
	static final String typeStr = "type";
	static final String healthStr = "          readinessProbe:\n" + "              httpGet:\n"
			+ "                scheme: HTTP\n" + "                path: /health\n" + "                port: 19001\n"
			+ "              initialDelaySeconds: 30\n" + "              periodSeconds: 5\n"
			+ "              timeoutSeconds: 5\n" + "              successThreshold: 1\n"
			+ "              failureThreshold: 10\n" + "          livenessProbe:\n" + "              httpGet:\n"
			+ "                scheme: HTTP\n" + "                path: /health\n" + "                port: 19001\n"
			+ "              initialDelaySeconds: 30\n" + "              periodSeconds: 60\n"
			+ "              timeoutSeconds: 10\n" + "              successThreshold: 1\n"
			+ "              failureThreshold: 3\n";
	static final String resourcesStr = "resources";
	static final String resourcesReqStr = "requests";
	static final String resourcesLimStr = "limits";
	static final String resourcesMemStr = "memory";
	static final String resourcesCpuStr = "cpu";
	static final String namespaceStr = "namespace";
	static final String namespaceDeltaStr = "delta";
	static final String configMapEnvStr = "envFrom";
	static final String configMapStr = "configMapRef";
	static final String secretValueStr = "valueFrom";
	static final String secretRefStr = "secretKeyRef";
	static final String secretKeyStr = "key";
	static final String secretDataStr = "data";
	
	public static void main(final String[] args) {
		YMLGenerator yg = new YMLGenerator();

		JCommander.newBuilder().addObject(yg).build().parse(args);
		writeYAML(yg);
	}

	private static void writeYAML(YMLGenerator yg) {
		if (yg.kind.equals("Deployment")) {
			System.out.println(apiVersionLbl + ": " + "apps/v1beta1");
		} else if (yg.kind.equals("Service") || yg.kind.equals("Secret")) {
			System.out.println(apiVersionLbl + ": " + "v1");
		}
		System.out.println(kindStr + ": " + yg.kind);
		// Meta Data
		System.out.println(metadataStr + ": ");
		System.out.println(INDENT + namespaceStr + ": " + namespaceDeltaStr);
		System.out.println(INDENT + labelStr + ": ");
		System.out.println(DBLINDENT + purposeStr + ": " + yg.label);
		System.out.println(INDENT + nameStr + ": " + yg.label);
		// Spec
		if (!yg.kind.equals("Secret"))System.out.println(specStr + ": ");

		if (yg.kind.equals("Deployment")) {
			System.out.println(INDENT + replicaStr + ": " + yg.replica);
			System.out.println(INDENT + templateStr + ": ");
			System.out.println(DBLINDENT + metadataStr + ": ");
			System.out.println(TRPLINDENT + labelStr + ": ");
			System.out.println(QUADINDENT + appStr + ": " + yg.label);
			System.out.println(DBLINDENT + specStr + ": ");
			System.out.println(TRPLINDENT + containersStr + ": ");
			// Envs
			System.out.println(QUADINDENT + "- " + envStr + ": ");
			yg.env.forEach(item -> {
				String[] envVar = item.split("=");
				System.out.println(QUININDENT + " - " + nameStr + ": " + envVar[0]);
				System.out.println(SEXINDENT + envValStr + ": " + envVar[1]);
			});
			// Secret
			/*
			 * System.out.println(QUININDENT + " - " + nameStr + ": " + envVar[0]); env: -
			 * name: SECRET_USERNAME valueFrom: secretKeyRef: name: mysecret key: username
			 */

			// Image
			System.out.println(QUININDENT + imageStr + ": " + "\"" + yg.image + "\"");
			System.out.println(QUININDENT + nameStr + ": " + yg.imageName);
			if (yg.health == 1) {
				System.out.println(healthStr);
			}
			// Working Dir
			System.out.println(QUININDENT + workDirStr + ": " + yg.workDir);
			// ConfigMap
			/*
			System.out.println(QUININDENT + configMapEnvStr + ": ");
			System.out.println(SEXINDENT + "  " + " - " + configMapStr + ": ");
			System.out.println(SEPTINDENT + "      " + nameStr + ": " + yg.configmap);
			*/
			// Resource Allocation
			if (!yg.memory.equals("")) {
				System.out.println(QUININDENT + resourcesStr + ": ");
				System.out.println(SEXINDENT + resourcesReqStr + ": ");
				String[] memVar = yg.memory.split(",");
				String[] cpuVar = yg.cpu.split(",");
				System.out.println(SEPTINDENT + resourcesMemStr + ": " + "\"" + memVar[0] + "\"");
				System.out.println(SEPTINDENT + resourcesCpuStr + ": " + "\"" + cpuVar[0] + "\"");
				System.out.println(SEXINDENT + resourcesLimStr + ": ");
				System.out.println(SEPTINDENT + resourcesMemStr + ": " + "\"" + memVar[1] + "\"");
				System.out.println(SEPTINDENT + resourcesCpuStr + ": " + "\"" + cpuVar[1] + "\"");
			}
			// Ports
			System.out.println(QUININDENT + portsStr + ": ");
			for (int i = 0; i < yg.ports.size(); i++) {
				System.out.println(SEXINDENT + "- " + containerPortStr + ": " + yg.ports.get(i));
			}
			// Vols
			System.out.println(QUININDENT + volMountStr + ": ");
			for (int i = 0; i < yg.vol.size(); i++) {
				System.out.println(SEXINDENT + "- " + mountPathStr + ": " + yg.vol.get(i).split("=")[0]);
				System.out.println(SEPTINDENT + " " + nameStr + ": " + "vol" + (i + 1));
			}
			System.out.println(TRPLINDENT + volumesStr + ": ");
			for (int i = 0; i < yg.vol.size(); i++) {
				System.out.println(QUADINDENT + "- " + hostPathStr + ": ");
				System.out.println(SEXINDENT + pathStr + ": " + yg.vol.get(i).split("=")[1]);
				System.out.println(QUININDENT + nameStr + ": " + "vol" + (i + 1));
			}
		} else if (yg.kind.equals("Service")) {
			// Ports
			System.out.println(INDENT + portsStr + ": ");
			for (int i = 0; i < yg.ports.size(); i++) {
				System.out.println(DBLINDENT + "- " + portStr + ": " + yg.ports.get(i));
			}
			if (!yg.targetPorts.equals("")) {
				System.out.println(DBLINDENT + "  " + targetPortStr + ": " + yg.targetPorts);
			}
			// External IP
			if (!yg.externalIP.equals("")) {
				System.out.println(INDENT + externalIPStr + ": ");
				System.out.println(DBLINDENT + "- " + yg.externalIP);
			}
			// Selector
			System.out.println(INDENT + selectorStr + ": ");
			System.out.println(DBLINDENT + "app" + ": " + yg.selector);
			// Type
			System.out.println(INDENT + typeStr + ": ");
			System.out.println(DBLINDENT + yg.type);
		} else if (yg.kind.equals("Secret")) {
			// Secret Name-Value pairs
			System.out.println(secretDataStr + ": ");
			yg.nv.forEach(item -> {
				String[] nvVar = item.split(":=");
				System.out.println(INDENT + nvVar[0] + ": " + nvVar[1]);
			});
		}
	}
}

/*
 * 
 * env: - name: SECRET_USERNAME valueFrom: secretKeyRef: name: mysecret key:
 * username
 * 
 * 
 */
