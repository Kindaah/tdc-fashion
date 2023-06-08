# The Dress Club Cookbooks - Azure

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
* Azure account
* A service principal 

### Step 0: Azure login as a Guest user
```sh
az login
```
Example of the expected output:
```json
[
  {
    "cloudName": "AzureCloud",
    "id": "xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "isDefault": true,
    "name": "Free Trial",
    "state": "Enabled",
    "tenantId": "xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "user": {
      "name": "user@domain.com",
      "type": "user"
    }
  }
]
```

### Step 1: Create a service principal
```sh
az ad sp create-for-rbac --query '{"client_id": appId, "secret": password "tenant": tenant}'
```
Example of the expected output:
```json
{
  "client_id": "xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "secret": "xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "tenant": "xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

### Obtain your Azure subscription ID
```sh
az account show --query "{ subscription_id: id }"
```
Example of the expected output:
```json
{
  "subscription_id": "xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

### Set env
```sh
export AZURE_SUBSCRIPTION_ID=xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
export AZURE_CLIENT_ID=xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
export AZURE_SECRET=xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
export AZURE_TENANT=xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```