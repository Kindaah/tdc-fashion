# Wordpress

```
#
# This source code is the confidential, proprietary information of
# Dress Club  here in, you may not disclose such Information,
# and may only use it in accordance with the terms of the license
# agreement you entered into with Dress Club.
#
# 2018: Dress Club.
# All Rights Reserved.
#
```

## How do I get set up?
### Requeriments
* VirtualBox, minimum version required 5.2.8
* Vagrant, minimum version required 2.0.4


### Clone the repository
```sh
$ git clone git@bitbucket.org:thedressclub/tdc-cookbooks.git
```

### Start the VM
```sh
$ vagrant up
```

## Useful commands
### Provision the VM
```sh
$ vagrant provision
```

### Stop the VM
```sh
$ vagrant halt
```

### Services running here:
* Mysql (v5.7), port: 3306
* Wordpress (v4.9.8) + PHP (v7.0), port: 8000

### Wordpress themes location
[`blog/var/www/html/wp-content/themes`](blog/var/www/html/wp-content/themes)

## Who do I talk to? ###
* Repo owner or admin
* Other community or team contact
