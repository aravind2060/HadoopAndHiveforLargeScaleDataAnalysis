# Creating an EC2 Instance on AWS

This guide will walk you through the steps to create an EC2 (Elastic Compute Cloud) instance on AWS (Amazon Web Services).

## Prerequisites

Before you begin, make sure you have the following:

- An AWS account
- Access to the AWS Management Console
- Basic understanding of AWS services

## Steps

1. **Sign in to the AWS Management Console**: Open your web browser and go to the [AWS Management Console](https://aws.amazon.com/console/). Sign in using your AWS account credentials.

2. **Navigate to EC2 Dashboard**: Once you're logged in, navigate to the EC2 Dashboard. You can find EC2 under the "Compute" section in the AWS Management Console.

3. **Launch Instance**: Click on the "Launch Instance" button to start the process of creating a new EC2 instance.

4. **Choose an Amazon Machine Image (AMI)**: Select the desired AMI for your instance. This is the operating system and software configuration for your EC2 instance.

5. **Choose an Instance Type**: Choose the instance type that suits your requirements in terms of CPU, memory, storage, and networking capacity.

6. **Configure Instance Details**: Configure additional settings such as the number of instances, network settings, IAM roles, and more.

7. **Add Storage**: Specify the size and type of storage (EBS volumes) to attach to your instance.

9. **Configure Security Group**: Create or select an existing security group to control the inbound and outbound traffic to your instance.

10. **Review and Launch**: Review all the configurations you've made for your instance. Once you're satisfied, click "Launch".

11. **Create a Key Pair**: If you haven't created a key pair before, you'll be prompted to create one. This key pair is necessary for accessing your EC2 instance securely.

12. **Launch Instance**: After creating the key pair, click "Launch Instances". Your EC2 instance will now be launched.

13. **Access Your Instance**: Once the instance is running, you can access it via SSH (for Linux instances) or RDP (for Windows instances) using the key pair you created.
