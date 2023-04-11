<div align="center">

# _FAIRY_

<i>A Java command line tool to make datasets findable on a global scale in a FAIR way</i>

![language](https://img.shields.io/badge/language-java-blue.svg)
[![license](https://img.shields.io/github/license/qbicsoftware/fairy-data-provider)](https://github.com/qbicsoftware/fairy-data-provider/blob/main/LICENSE)

</div>

This tool can be used to make datasets **findable** on the World Wide Web.  
FAIRY creates landing pages for the provided datasets. 
These landing pages contain Schema.org markup with properties proposed by the Bioschemas.org dataset profile.
The landing pages and the dataset markup can then be found by search engines.

The website hosting all the landing pages is [the FAIRY Website](https://fair.qbic.uni-tuebingen.de/).    
All dataset landing pages are accessible from this page: [Dataset Navigation Page](https://fair.qbic.uni-tuebingen.de/datasets/)

## How to run _FAIRY_
### Download
Soon _FAIRY_'s compiled and executable Java binaries as JAR will be available on this repository.
With the JAR file, _FAIRY_ can be used with a few easy commands and options.

### Requirements 
To use the tool, you need to be allowed to connect to the server that hosts the landing pages. A connection to this server is also only possible from within the VPN of the University of Tübingen.

If you are allowed to transfer files to the server, you will automatically authenticate with ssh key authentication by only providing your username.
For this to work, your private and public keys need to be saved in the default locations `~/.ssh/id_rsa` and `~/.ssh/id_rsa.pub`.

Before running _FAIRY_ the first time, the hosts fingerprint needs to be added to your known_hosts file. 
Here, the host is `fair.qbic.uni-tuebingen.de`.
To add this host to your known_hosts file, the following line can be run in the command line: 
````
ssh-keyscan -t rsa fair.qbic.uni-tuebingen.de >> ~/.ssh/known_hosts
 ````

Additionally, you need to have **Java JRE** or **JDK** installed to run the tool.

## How to use _FAIRY_
Since _FAIRY_ is a command line tool, it can be run by providing commands and options together with the _FAIRY_ JAR in the command line.
To get an overview of all available options and commands, `java -jar fairy.jar -h` can be run. This produces the following output:
````
Usage: [COMMAND] -u=<username> -f=<tsvFile> [-hV]
-f, --file=<tsvFile>    The path to a tsv file describing dataset metadata
-h, --help              Show this help message and exit.
-u, --username=<user>   Username for connecting to the server
-V, --version           Print version information and exit.
Commands:
create  Create a new landing page for datasets
````

### The `Create` Command
To create landing pages for datasets, the `create` command needs to be used.
For this command to work, there are two options necessary.
The path to a file needs to be provided with the `-f or --file` option. Also, a username needs to be given with `-u or --user`.

An example for a full create command would be:  
````
Java -jar fairy.jar create -f "../Example.tsv" -u username
````

With this command, _FAIRY_ will create landing pages with the information specified in the Example.tsv file for each dataset represented in this file.

### Input file 
The file that needs to be provided with the `-f or --file` option currently needs to be in **TSV format**.
The property that definitely needs to be provided for the tool to work is **identifier**. This property should uniquely identify the corresponding dataset in this context.
Further properties that are currently supported by _FAIRY_ and should at least be added to provide rich metadata for the datasets are:
- _description_ : Text, at least 50 characters
- _name_ : Text 
- _license_ : URL
- _keywords_ : Text, divided by comma
- _creator_ : Text
- _measurementTechnique_ : Text
- _dateCreated_ : Date

These properties will be represented in the landing page markup in their expected types.
Other properties describing datasets can also be provided, but they will only be represented as type text in the markup.

The TSV-file needs to have the Schema.org property names in the first line.
Every following line represents the metadata for one dataset.
An example for such a file can be found here: [Example for a TSV metadata file](ExampleInputFile.tsv)

## Additional Information
### Proof-of-concept Implementation

This tool is currently a prototype that was created as part of a bachelor thesis.
Therefore, it does only have functionalities needed to make the tool work and to proof the concept that was worked out in the thesis and not any further.

### License
_FAIRY_ can be used under the MIT license.  
Other frameworks used for this tool have the following licenses:

- Jackson: [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- Apache FreeMarker: [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- Picocli: [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- Spock: [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- Apache Maven: [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- JSch: [BSD-style license, 3 clauses](http://www.jcraft.com/jsch/LICENSE.txt)
- Bootstrap: [MIT License (MIT)](https://github.com/twbs/bootstrap/blob/v4.0.0/LICENSE)