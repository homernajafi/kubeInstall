# kubeInstall
Ansible Installation/Setup script to setup Kubernetes Cluster running only one command
This is only tested on RedHat/CentOS. I will update it later for Ubuntu

This line will:
- Cleanup any old, broken installation of Docker and Kubernetes
- Install new Docker and all Kubernetes tools/programs
- It does not have any dependencies to other software
- No Download or setup is needed except setting up Ansible (below) if it's not already installed 
- It will install a single Master Cluster
- Number of worker nodes can be anything

The command is: ansible-playbook -s installKubernetes.yml 
Setting up Ansible before running the command above (Everything is done under 'root' user):
yum install epel-release (if this doesn't work use this: wget https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm   ;    rpm -i epel-release-latest-7.noarch.rpm ; yum update)

yum install ansible
vi /etc/ansible/hosts
Add IP address of your servers to hosts file like the following:
[k8scluster]
10.10.10.1
10.10.10.2
10.10.10.3
[local]
10.10.10.1

Setup SSH Key from Master node (the node you are planning to make Master) to other nodes (This if you don't know how:  http://www.linuxproblem.org/art_9.html) and Test it (make sure you won't be prompted for 'Yes' reply.

Done. Run the ansible-playbook command mentioned above and test the installation with 'kubectl get nodes' on master after installation is done.

Cleanup
If you don't want K8s on your boxes anymore just run 'kubeadm reset' on them and it will go away.
