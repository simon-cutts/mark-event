# mark-event

![mark](mark-api.png)

This applications depicted above, mark-api and mark-event, are intended as a strawman to demonstrate the benefits of a light-weight, purely serverless event sourcing system. Event sourcing stores every state change to the application as an event object. These event objects are stored in the sequence they were applied for the lifetime of the application.

This mark-event application has a companion application, [mark-api](https://github.com/simon-cutts/mark-api). The mark-api app is a microservice managing marks; mark-event is an application consuming the events produced from mark-api.

Serverless was chosen to simplify the infrastructure with minimal dev ops; but, just as importantly, to use native cloud services rather than rely non-trivial specialist event sourced application frameworks. 

### Event Stream

Events traverse the event stream to notify downstream clients. The event stream is a combination of mark-api and mark-event; its transactional and comprises:

1. From mark-api, a DynamoDB stream from the `RegistrationNumberEvent` table, emits transactional, reliable, time ordered sequence of events. Events remain in `RegistrationNumberEvent` for the lifetime of the mark-api application 
2. The `DynamoDbStreamProcessor` lambda in the mark-event app picks data off the DynamoDB stream and reassembles it into a JSON representation of the event. This event is then written to the kinesis stream`KinesisStream` within mark-event.
3. The `KinesisStream` from mark-event maintains the same time ordered sequence of events that can be fanned out to multiple interested clients
4. The `KinesisStreamS3Processor` lambda within mark-event is an example of a HTTP/2 Kinesis client using [enhanced fan-out](https://docs.aws.amazon.com/streams/latest/dev/introduction-to-enhanced-consumers.html) to read from the stream. It writes the events to S3. Multiple other enhanced fan-out Lambdas could also access the same stream, acting independently of each other, maintaining their own transactional view of the stream 

### Outstanding Tasks

Stuff for the next iteration:

1. Downstream client (KinesisStreamS3Processor) is not idempotent
2. Consider rewriting with TypeScript - the cold start times are 6 seconds!

## Installation
The application can be deployed in an AWS account using the [Serverless Application Model (SAM)](https://github.com/awslabs/serverless-application-model). 

The example `KinesisStreamS3Processor` lambda client needs an S3 bucket created to store a copy of all events sent to the application. The `template.yml` file in the root folder contains the application definition. Once you've created the bucket, update the bucket name in `template.yml` file, replacing
```
  EventBucket:
    Type: 'AWS::S3::Bucket'
    Properties:
      BucketName: "mark-event"
```
with
```
  EventBucket:
    Type: 'AWS::S3::Bucket'
    Properties:
      BucketName: "<YOUR EVENT BUCKET NAME>"
```
To build and install the mark-api application you will need [AWS CLI](https://aws.amazon.com/cli/), [SAM](https://github.com/awslabs/serverless-application-model) and [Maven](https://maven.apache.org/) installed on your computer.

Once they have been installed, from the shell, navigate to the root folder of the app and use maven to build a deployable jar. 
```
$ mvn clean package
```

This command should generate a `mark-event.jar` in the `target` folder. Now that we have generated the jar file, we can use SAM to package the sam for deployment. 

You will need a deployment S3 bucket to store the artifacts for deployment. Once you have created the deployment S3 bucket, run the following command from the app root folder:

```
$ sam package --output-template-file packaged.yml --s3-bucket <YOUR DEPLOYMENT S3 BUCKET NAME>

Uploading to xxxxxxxxxxxxxxxxxxxxxxxxxx  6464692 / 6464692.0  (100.00%)
Successfully packaged artifacts and wrote output template to file output-template.yaml.
Execute the following command to deploy the packaged template
aws cloudformation deploy --template-file /your/path/output-template.yml --stack-name <YOUR STACK NAME>
```

You can now use the cli to deploy the application. Choose a stack name and run the `sam deploy` command.
 
```
$ sam deploy --template-file ./packaged.yml --stack-name <YOUR STACK NAME> --capabilities CAPABILITY_IAM
```

Once the application is deployed, you can describe the stack thus:

```
$ aws cloudformation describe-stacks --stack-name <YOUR STACK NAME>
{
    "Stacks": [
        {
            "StackId": "arn:aws:cloudformation:eu-west-2:022099488461:stack/mark-event/dccde1a0-1518-11ea-8ce6-02bbe2e31b38",
            "StackName": "mark-event",
            "ChangeSetId": "arn:aws:cloudformation:eu-west-2:022099488461:changeSet/awscli-cloudformation-package-deploy-1575303250/d87be7c8-ac7a-4c44-a051-e7dd98185c1c",
            "Description": "AWS SomApiApp API - mark-event::mark-event-app",
            "CreationTime": "2019-12-02T15:37:28.681Z",
            "LastUpdatedTime": "2019-12-02T16:14:15.624Z",
            "RollbackConfiguration": {},
            "StackStatus": "CREATE_COMPLETE",
            "DisableRollback": false,
            "NotificationARNs": [],
            "Capabilities": [
                "CAPABILITY_IAM"
            ],
            "Outputs": [
                {
                    "OutputKey": "ConsumerARN",
                    "OutputValue": "arn:aws:kinesis:eu-west-2:xxxxxxxxx:stream/RegistrationNumberEventKinesisStream/consumer/RegistrationNumberEventStreamConsumer:1575303290",
                    "Description": "Stream consumer ARN"
                },
                {
                    "OutputKey": "StreamARN",
                    "OutputValue": "arn:aws:kinesis:eu-west-2:xxxxxxx:stream/RegistrationNumberEventKinesisStream",
                    "Description": "Kinesis Stream ARN"
                }
            ],
            "Tags": [],
            "EnableTerminationProtection": false,
            "DriftInformation": {
                "StackDriftStatus": "NOT_CHECKED"
            }
        }
    ]
}
```

If any errors were encountered, examine the stack events to diagnose the issue

```
$ aws cloudformation describe-stack-events --stack-name <YOUR STACK NAME>
```

At any time, you may delete the stack

```
$ aws cloudformation delete-stack --stack-name <YOUR STACK NAME>
```