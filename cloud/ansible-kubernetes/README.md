# Setup a kubernetes cluster from scratch using atomic-host 

## Prerequisites

Before all, 
- generate a key `id_rsa`, an `id_rsa.pub` and place it in this folder
- download an atomic host image, import in virtualbox, 
- create a virtual machine, and set the first network instance to Bridged network
- then create snapshot named atomic-template-snapshot

## Create vms

Create a new host with `./vbox.sh <N>`, then add manually the new ip in the inventory file

Create a new host in amazon `./aws.sh <N>`, then add manually the new ip in the inventory file

