What is DLAB?
=============

DLab is an essential toolset for analytics. It is a self-service Web Console, used to create and manage exploratory environments. It allows teams to spin up analytical environments with best of breed open-source tools just with a single click of the mouse. Once established, environment can be managed by an analytical team itself, leveraging simple and easy-to-use Web Interface.
<p>See more at <a href="http://dlab.opensource.epam.com/" rel="nofollow">dlab.opensource.epam.com</a>.</p>

------------
## CONTENTS
-----------

[Login](#login)

[Create project](#setup_edge_node)

[Setting up analytical environment and managing computational power](#setup_environmen)

&nbsp; &nbsp; &nbsp; &nbsp; [Create notebook server](#notebook_create)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; [Manage libraries](#manage_libraries)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; [Create image](#create_image)

&nbsp; &nbsp; &nbsp; &nbsp; [Stop Notebook server](#notebook_stop)

&nbsp; &nbsp; &nbsp; &nbsp; [Terminate Notebook server](#notebook_terminate)

&nbsp; &nbsp; &nbsp; &nbsp; [Deploy Computational resource](#computational_deploy)

&nbsp; &nbsp; &nbsp; &nbsp; [Stop Apache Spark cluster](#spark_stop)

&nbsp; &nbsp; &nbsp; &nbsp; [Terminate Computational resource](#computational_terminate)

&nbsp; &nbsp; &nbsp; &nbsp; [Collaboration space](#collaboration_space)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; [Manage Git credentials](#git_creds)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; [Git UI tool (ungit)](#git_ui)

[Administration](#administration)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;[Manage roles](#manage_roles)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;[Environment Management Page](#environment_management)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; [Manage environment](#manage_environment)

[DLab billing report](#billing_page)



[Web UI filters](#filter)

[Scheduler](#scheduler)

[Key reupload](#key_reupload)

---------
# Login <a name="login"></a>

As soon as DLab is deployed by an infrastructure provisioning team and you received DLab URL, your username and password – open DLab login page, fill in your credentials and hit Login.

DLab Web Application authenticates users against:

-   OpenLdap;
-   Cloud Identity and Access Management service user validation;

| Login error messages               | Reason                                                                           |
|------------------------------------|----------------------------------------------------------------------------------|
| Username or password is invalid |The username provided:<br>doesn’t match any LDAP user OR<br>there is a type in the password field |
| Please contact AWS administrator to create corresponding IAM User | The user name provided:<br>exists in LDAP BUT:<br>doesn’t match any of IAM users in AWS |
| Please contact AWS administrator to activate your Access Key      | The username provided:<br>exists in LDAP BUT:<br>IAM user doesn’t have a single Access Key\* created OR<br>IAM user’s Access Key is Inactive |

\* Please refer to official documentation from Amazon to figure out how to manage Access Keys for your AWS Account: http://docs.aws.amazon.com/general/latest/gr/managing-aws-access-keys.html

To stop working with DLab - click on Log Out link at the top right corner of DLab.

After login user will see warning in case of exceeding quota or close to this limit.

<p align="center" class="facebox-popup"> 
    <img src="doc/exceeded quota.png" alt="Exceeded quota" width="400">
</p>

<p align="center" class="facebox-popup"> 
    <img src="doc/close to limit.png" alt="Close to limit" width="400">
</p>

----------------------------------
# Create project <a name="setup_edge_node"></a>

When you log into DLab Web Application, the first thing you will have to create new project.

To do this click on “Upload” button on “Projects” page, select your personal public key and hit “Create” button or click on "Generate" button on “Projects” and save your private key.

<p align="center" class="facebox-popup"> 
    <img src="doc/upload_or_generate_user_key.png" alt="Upload or generate user key" width="100%">
</p>

Please note that you need to have a key pair combination (public and private key) to work with DLab. To figure out how to create public and private key, please click on “Where can I get public key?” on “Projects” page. DLab build-in wiki page will guide Windows, MasOS and Linux on how to generate SSH key pairs quickly.

After you hit "Create" or "Generate" button, creation of Project will start. This process is a one-time operation for each Data Scientist and it might take up-to 10 minutes for DLab to setup initial infrastructure for you. During this process, project will have status creating.

As soon as an Project is created, Data Scientist can create  notebook server on “List of Resources” page. The message “To start working, please create new environment” will be displayed:

![Main page](doc/main_page.png)

---------------------------------------------------------------------------------------
# Setting up analytical environment and managing computational power <a name="setup_environmen"></a>


## Create notebook server <a name="notebook_create"></a>

To create new analytical environment from “List of Resources” page click on "Create new" button.

The "Create analytical tool" popup will show-up. Data Scientist can choose the preferred project, endpoint and analytical tool to be setup. Adding new analytical tools is supported by architecture, so you can expect new templates to show up in upcoming releases.
Currently by means of DLab, Data Scientists can select between any of the following templates:

-   Jupyter
-   Apache Zeppelin
-   RStudio
-   RStudio with TensorFlow
-   Jupyter with TensorFlow
-   Deep Learning (Jupyter + MXNet, Caffe, Caffe2, TensorFlow, CNTK, Theano, Torch and Keras)

<p align="center"> 
    <img src="doc/notebook_create.png" alt="Create notebook" width="574">
</p>

After specifying desired template, you should fill in the “Name” and “Instance shape”.

Name field – is just for visual differentiation between analytical tools on “List of resources” dashboard.

Instance shape dropdown, contains configurable list of shapes, which should be chosen depending on the type of analytical work to be performed. Following groups of instance shapes will be showing up with default setup configuration:

<p align="center"> 
    <img src="doc/select_shape.png" alt="Select shape" width="250">
</p>

These groups have T-Shirt based shapes (configurable), that can help Data Scientist to either save money\* and leverage not very powerful shapes (for working with relatively small datasets), or that could boost the performance of analytics by selecting more powerful instance shape.

\* Please refer to official documentation from Amazon that will help you understand what [instance shapes](https://aws.amazon.com/ec2/instance-types/) would be most preferable in your particular DLAB setup. Also, you can use [AWS calculator](https://calculator.s3.amazonaws.com/index.html) to roughly estimate the cost of your environment.

You can override the default configurations for local spark. The configuration object is referenced as a JSON file. To tune spark configuration check off "Spark configurations" check box and insert JSON format in text box.

After you Select the template, fill in the Name and choose needed instance shape - you need to click on "Create" button for your instance to start creating. Corresponding record will show up in your dashboard:

![Dashboard](doc/main_page2.png)

As soon as notebook server is created, its status will change to Running:

![Running notebook](doc/main_page3.png)

When you click on the name of your Analytical tool in the dashboard – analytical tool popup will show up:

<p align="center"> 
    <img src="doc/notebook_info.png" alt="Notebook info" width="574">
</p>

In the header you will see version of analytical tool, its status and shape.

In the body of the dialog:

-   Up time
-   Analytical tool URL
-   Git UI tool (ungit)
-   Shared bucket for all users
-   Bucket that has been provisioned for your needs

To access analytical tool Web UI you proceed with one of the options:

-   use direct URL's to access notebooks (your access will be established via reverse proxy, so you don't need to have Edge node tunnel up and running)
-   SOCKS proxy based URL's to access notebooks (via tunnel to Edge node)

If you use direct urls you don't need to open tunnel for Edge node and set SOCKS proxy.
If you use indirect urls you need to configure SOCKS proxy and open tunnel for Edge node. Please follow the steps described on “Read instruction how to create the tunnel” page to configure SOCKS proxy for Windows/MAC/Linux machines. “Read instruction how to create the tunnel” is available on DLab notebook popup.

### Manage libraries <a name="manage_libraries"></a>

On every analytical tool instance you can install additional libraries by clicking on gear icon <img src="doc/gear_icon.png" alt="gear" width="20"> in the Actions column for a needed Notebook and hit Manage libraries:

<p align="center"> 
    <img src="doc/notebook_menu_manage_libraries.png" alt="Notebook manage_libraries" width="150">
</p>

After clicking you will see the window with 3 fields:
-   Field for selecting an active resource to install libraries on
-   Field for selecting group of packages (apt/yum, Python 2, Python 3, R, Java, Others)
-   Field for search available packages with autocomplete function except for Java. java library you should enter using the next format: "groupID:artifactID:versionID"

![Install libraries dialog](doc/install_libs_form.png)

You need to wait for a while after resource choosing till list of all available libraries will be received.

![Libraries list loading](doc/notebook_list_libs.png)

**Note:** apt or yum packages depends on your DLab OS family.

**Note:** In group Others you can find other Python (2/3) packages, which haven't classifiers of version.

![Resource select_lib](doc/notebook_select_lib.png)

After selecting library, you can see it on the right and could delete in from this list before installing.

![Resource selected_lib](doc/notebook_selected_libs.png)

After clicking on "Install" button you will see process of installation with appropriate status.

![Resources libs_status](doc/notebook_libs_status.png)

**Note:** If package can't be installed you will see "Failed" in status column and button to retry installation.

### Create image <a name="create_image"></a>

Out of each analytical tool instance you can create an AMI image (notebook should be in Running status), including all libraries, which have been installed on it. You can use that AMI to speed-up provisioining of further analytical tool, if you would like to re-use existing configuration. To create an AMI click on a gear icon <img src="doc/gear_icon.png" alt="gear" width="20"> in the Actions menu for a needed Notebook and hit "Create AMI":

<p align="center"> 
    <img src="doc/notebook_menu_create_ami.png" alt="Notebook create_ami" width="150">
</p>

On Create AMI popup you will be asked to fill in:
-   text box for an AMI name (mandatory)
-   text box for an AMI description (optional)

<p align="center"> 
    <img src="doc/create_ami.png" alt="Create AMI" width="480">
</p>

After clicking on "Assign" button the Notebook status will change to Creating AMI. Once an image is created the Notebook status changes back to Running.

To create new analytical environment from custom image click "Create new" button on “List of Resources” page. 

“Create analytical tool” popup will show-up. Choose a template of a Notebook for which the custom image is created:

<p align="center"> 
    <img src="doc/create_notebook_from_ami.png" alt="Create notebook from AMI" width="560">
</p>

Before clicking "Create" button you should choose the image from "Select AMI" and fill in the "Name" and "Instance shape".

--------------------------
## Stop Notebook server <a name="notebook_stop"></a>

Once you have stopped working with an analytical tool and you would like to release cloud resources for the sake of the costs, you might want to Stop the notebook. You will be able to Start the notebook again after a while and proceed with your analytics.

To Stop the Notebook click on a gear icon <img src="doc/gear_icon.png" alt="gear" width="20"> in the Actions column for a needed Notebook and hit Stop:

<p align="center"> 
    <img src="doc/notebook_menu_stop.png" alt="Notebook stopping" width="150">
</p>

Hit OK in confirmation popup.

**NOTE:** if any Computational resources except for Spark cluster have been connected to your notebook server – they will be automatically terminated if you stop the notebook and Spark cluster will be automatically stopped.

<p align="center"> 
    <img src="doc/notebook_stop_confirm.png" alt="Notebook stop confirm" width="400">
</p>

After you confirm you intent to Stop the notebook - the status will be changed to Stopping and will become Stopped in a while. Spark cluster status will be changed to Stopped and other Computational resource status  will be changed to Terminated.

--------------------------------
## Terminate Notebook server <a name="notebook_terminate"></a>

Once you have finished working with an analytical tool and you would like to release cloud resources for the sake of the costs, you might want to Terminate the notebook. You will not be able to Start the notebook which has been Terminated. Instead, you will have to create new Notebook server if you will need to proceed your analytical activities.

To Terminate the Notebook click on a gear icon <img src="doc/gear_icon.png" alt="gear" width="20"> in the Actions column for a needed Notebook and hit Terminate:

**NOTE:** if any Computational resources have been linked to your notebook server – they will be automatically terminated if you stop the notebook.

Confirm termination of the notebook and afterward notebook status will be changed to **Terminating**:

![Notebook terminating](doc/notebook_terminating.png)

Once corresponding instances are terminated on cloud, status will finally
change to Terminated:

![Notebook terminated](doc/notebook_terminated.png)

---------------
## Deploy Computational resource <a name="computational_deploy"></a>

After deploying Notebook node, you can deploy Computational resource and it will be automatically linked with your Notebook server. Computational resource is a managed cluster platform, that simplifies running big data frameworks, such as Apache Hadoop and Apache Spark on cloud to process and analyze vast amounts of data. Adding Computational resource is not mandatory and is needed in case computational resources are required for job execution.

On “Create Computational Resource” popup you will have to choose Computational resource version (configurable) and specify alias for it. To setup a cluster that meets your needs – you will have to define:

-   Total number of instances (min 2 and max 14, configurable);
-   Master and Slave instance shapes (list is configurable and supports all available cloud instance shapes, supported in your cloud region);

Also, if you would like to save some costs for your Computational resource you can create it based on [spot instances](https://aws.amazon.com/ec2/spot/), which are often available at a discount price (this functionality is only available for AWS cloud):

-   Select Spot Instance checkbox;
-   Specify preferable bid for your spot instance in % (between 20 and 90, configurable).

**NOTE:** When the current Spot price rises above your bid price, the Spot instance is reclaimed by cloud so that it can be given to another customer. Please make sure to backup your data on periodic basis.

This picture shows menu for creating Computational resource for AWS:
<p align="center"> 
    <img src="doc/emr_create.png" alt="Create Computational resource on AWS" width="760">
</p>

You can override the default configurations for applications by supplying a configuration object for applications when you create a cluster (this functionality is only available for Amazon EMR cluster ). The configuration object is referenced as a JSON file.
To tune computational resource configuration check off "Cluster configurations" check box and insert JSON format in text box:

<p align="center"> 
    <img src="doc/emr_create_configuration.png" alt="Create Custom Computational resource on AWS" width="760">
</p>

This picture shows menu for creating Computational resource for Azure:
<p align="center"> 
    <img src="doc/dataengine_creating_menu.png" alt="Create Computational resource on Azure" width="760">
</p>

If you click on "Create" button Computational resource creation will kick off. You will see corresponding record on DLab Web UI in status **Creating**:

![Creating Computational resource](doc/emr_creating.png)

Once Computational resources are provisioned, their status will be changed to **Running**.

Clicking on Computational resource name in DLab dashboard will open Computational resource details popup:

<p align="center"> 
    <img src="doc/emr_info.png" alt="Computational resource info" width="480">
</p>

Also you can go to computational resource master UI via link "Apache Spark Master' or "EMR Master" (this functionality is only available for AWS cloud).

Since Computational resource is up and running - you are now able to leverage cluster computational power to run your analytical jobs on.

To do that open any of the analytical tools and select proper kernel/interpreter:

**Jupyter** – goto Kernel and choose preferable interpreter between local and Computational resource ones. Currently we have added support of Python 2/3, Spark, Scala, R into Jupyter.

![Jupiter](doc/jupiter.png)

**Zeppelin** – goto Interpreter Biding menu and switch between local and Computational resource there. Once needed interpreter is selected click on Save.

![Zeppelin](doc/zeppelin.png)

Insert following “magics” before blocks of your code to start executing your analytical jobs:

-   interpreter\_name.%spark – for Scala and Spark;
-   interpreter\_name.%pyspark – for Python2;
-   interpreter\_name.%pyspark3 – for Python3;
-   interpreter\_name.%sparkr – for R;

**RStudio –** open R.environ and comment out /opt/spark/ to switch to Computational resource and vise versa to switch to local kernel:

![RStudio](doc/rstudio.png)

---------------
## Stop  Apache Spark cluster <a name="spark_stop"></a>

Once you have stopped working with a spark cluster and you would like to release cloud resources for the sake of the costs, you might want to Stop Apache Spark cluster. You will be able to Start apache Spark cluster again after a while and proceed with your analytics.

To Stop Apache Spark cluster click on <img src="doc/stop_icon.png" alt="stop" width="20"> button close to spark cluster alias.

Hit "YES" in confirmation popup.

<p align="center"> 
    <img src="doc/spark_stop_confirm.png" alt="Spark stop confirm" width="400">
</p>

After you confirm your intent to Apache Spark cluster - the status will be changed to Stopping and will become Stopped in a while.

------------------
## Terminate Computational resource <a name="computational_terminate"></a>

To release cluster computational resources click on <img src="doc/cross_icon.png" alt="cross" width="16"> button close to Computational resource alias. Confirm decommissioning of Computational resource by hitting Yes:

<p align="center"> 
    <img src="doc/emr_terminate_confirm.png" alt="Computational resource terminate confirm" width="400">
</p>

In a while Computational resource cluster will get **Terminated**. Corresponding cloud instances will also removed on cloud.

--------------------------------
## Collaboration space <a name="collaboration_space"></a>

### Manage Git credentials <a name="git_creds"></a>

To work with Git (pull, push) via UI tool (ungit) you could add multiple credentials in DLab UI, which will be set on all running instances with analytical tools.

When you click on the button "Git credentials" – following popup will show up:

<p align="center"> 
    <img src="doc/git_creds_window.png" alt="Git_creds_window" width="760">
</p>

In this window you need to add:
-   Your Git server hostname, without **http** or **https**, for example: gitlab.com, github.com, or your internal GitLab server, which can be deployed with DLab.
-   Your Username and Email - used to display author of commit in git.
-   Your Login and Password - for authorization into git server.

**Note:** If you have GitLab server, which was deployed with DLab, you should use your LDAP credentials for access to GitLab.

Once all fields are filled in and you click on "Assign" button, you will see the list of all your Git credentials.

Clicking on "Apply changes" button, your credentials will be sent to all running instances with analytical tools. It takes a few seconds for changes to be applied.

<p align="center"> 
    <img src="doc/git_creds_window2.png" alt="Git_creds_window1" width="760">
</p>

On this tab you can also edit your credentials (click on pen icon) or delete (click on bin icon).

### Git UI tool (ungit) <a name="git_ui"></a>
Confirm you want to do backup by clicking "Apply".
On every analytical tool instance you can see Git UI tool (ungit):

<p align="center"> 
    <img src="doc/notebook_info.png" alt="Git_ui_link" width="520">
</p>

Before start working with git repositories, you need to change working directory on the top of window to:

**/home/dlab-user/** or **/opt/zeppelin/notebook** for Zeppelin analytical tool and press Enter.

**Note:** Zeppelin already uses git for local versioning of files, you can add upstream for all notebooks.

After changing working directory you can create repository or better way - clone existing:

![Git_ui_ungit](doc/ungit_window.png)

After creating repository you can see all commits and branches:

![Git_ui_ungit_work](doc/ungit_work.png)

On the top of window in the red field UI show us changed or new files to commit. You can uncheck or add some files to gitignore.

**Note:** Git always checks you credentials. If this is your first commit after adding/changing credentials and after clicking on "Commit" button nothing happened - just click on "Commit" button again.

On the right pane of window you also can see buttons to fetch last changes of repository, add upstreams and switch between branches.

To see all modified files - click on the "circle" button on the center:

![Git_ui_ungit_changes](doc/ungit_changes.png)

After commit you will see your local version and remote repository. To push you changes - click on your current branch and press "Push" button.

![Git_ui_ungit_push](doc/ungit_push.png)

Also clicking on "circle" button you can uncommit or revert changes.

--------------------------------
# Administration <a name="administration"></a>


## Manage roles <a name="manage_roles"></a>

Administrator can choose what instance shape(s) and notebook(s) can be allowed for certain group(s) or user(s).
To do it click on "Manage roles" button. "Manage roles" popup will show-up:

<p align="center"> 
    <img src="doc/manage_role.png" alt="Manage roles" width="780">
</p>

To add group enter group name, choose certain action which should be allowed for group and also you can add discrete user(s) (not mandatory) and then click "Create" button.
New group will be added and appears on "Manage roles" popup.

Administrator can remove group or user. For that you should only click on "Delete group" button for certain group or click on delete icon <img src="doc/delete_btn.png" alt="delete" width="16"> for particular user. After that Hit "Yes" in confirmation popup.

<p align="center"> 
    <img src="doc/delete_group.png" alt="Delete group" width="780">
</p>

--------------------------------

## Environment Management Page <a name="environment_management"></a>

DLab Environment Management page is an administration page allowing admins to show the list of all users` environments and to stop/terminate all of them of separate specific resource.

To access Environment management page either navigate to it via main menu:

<p align="center"> 
    <img src="doc/environment_management.png" alt="Environment management">
</p>

To Stop or Terminate the Notebook click on a gear icon gear in the Actions column for a needed Notebook and hit Stop or Terminate action:
<p align="center"> 
    <img src="doc/manage_env_actions.png" alt="Manage environment actions" width="160">
</p>

Any Computational resources except for Spark clusters will be automatically terminated and Spark clusters will be stopped in case of Stop action hitting, and all resources will be killed in case of Terminate action hitting.

To stop or release specific cluster click an appropriate button close to cluster alias.

<p align="center"> 
    <img src="doc/managemanage_resource_actions.png" alt="Manage resource action" width="300">
</p>

Confirm stopping/decommissioning of the Computational resource by hitting Yes:

<p align="center"> 
    <img src="doc/manage_env_confirm.png" alt="Manage environment action confirm" width="400">
</p>

**NOTE:** terminate action is available only for notebooks and computational resources, not for Edge Nodes.

### Manage environment <a name="manage_environment"></a>

Administrator can manage users environment clicking on Manage environment button. "Manage environment" popup will show-up. All users environments will be shown which at least one instance has Running status:

<p align="center"> 
    <img src="doc/manage_environment.png" alt="Manage environment" width="520">
</p>

--------------------------------

# DLab Billing report <a name="billing_page"></a>

On this page you can see all billing information, including all costs assosiated with service base name of SSN.

![Billing page]
(doc/billing_page.png)

In the header you can see 3 fields:
-   Service base name of your environment
-   Resource tag ID
-   Date period of available billing report

On the center of header you can choose period of report in datepicker:

<p align="center"> 
    <img src="doc/billing_datepicker.png" alt="Billing datepicker" width="400">
</p>

You can save billing report in csv format hitting "Export" button.

You can also filter data by each column:

![Billing filter](doc/billing_filter.png)

**Note:** Administrator can see billing report of all users, and only he can see/filter "User" column.

In the footer of billing report, you can see Total cost for all environments.

--------------------------------

# Web UI filters <a name="filter"></a>

You can leverage functionality of build-in UI filter to quickly manage the analytical tools and computational resources, which you only want to see in your dashboard.

To do this, simply click on icon <img src="doc/filter_icon.png" alt="filter" width="16"> in dashboard header and filter your list by any of:

-   environment name (input field);
-   status (multiple choice);
-   shape (multiple choice);
-   computational resources (multiple choice);

![Main page filter](doc/main_page_filter.png)

Once your list of filtered by any of the columns, icon <img src="doc/filter_icon.png" alt="filter" width="16"> changes to <img src="doc/sort_icon.png" alt="filter" width="16"> for a filtered columns only.

There is also an option for quick and easy way to filter out all inactive instances (Failed and Terminated) by clicking on “Show active” button in the ribbon. To switch back to the list of all resources, click on “Show all”.

# Scheduler <a name="scheduler"></a>

Scheduler component allows to automatically schedule start/stop of notebook/cluster. There are 2 types of schedulers available:
- notebook scheduler;
- data engine scheduler (currently spark cluster only);

To create scheduler for a notebook click on a <img src="doc/gear_icon.png" alt="gear" width="20"> icon in the Actions column for a needed Notebook and hit Scheduler:

<p align="center"> 
    <img src="doc/notebook_menu_scheduler.png" alt="Notebook scheduler action" width="150">
</p>
After clicking you will see popup with the following fields:

- start/finish dates - date range when scheduler is active;
- start/end time - time when notebook should be running;
- offset - your zone offset;
- repeat on - days when scheduler should be active
- possibility to synchronize notebook scheduler with computational schedulers

<p align="center"> 
    <img src="doc/notebook_scheduler.png" alt="Notebook scheduler" width="400">
</p>

Also scheduler can be configured for a spark cluster. To configure scheduler for spark cluster <img src="doc/icon_scheduler_computational.png" alt="scheduler_computational" width="16"> should be clicked (near computational status):

<p align="center"> 
    <img src="doc/computational_scheduler_create.png" alt="Computational scheduler create" width="400">
</p>

There is a possibility to inherit scheduler start settings from notebook, if such scheduler is present:

<p align="center"> 
    <img src="doc/computational_scheduler.png" alt="Computational scheduler" width="400">
</p>

Once any scheduler is set up, notebook/spark cluster will be started/stopped automatically.
Please also note that if notebook is configured to be stopped, all running data engines assosiated with it will be stopped (for spark cluster) or terminated (for data engine serice) with notebook.

After login user will be notified  that corresponding resources are about to be stopped/terminated in some time.

<p align="center"> 
    <img src="doc/scheduler reminder.png" alt="Scheduler reminder" width="400">
</p>