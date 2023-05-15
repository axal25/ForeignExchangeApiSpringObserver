# Foreign Exchange API - with Spring Observer pattern

A recruitment task

### Application requirements

1. Java 8+
2. Maven

### Application run

1. Compile package  
   `mvn clean package`
    1. Add `-DskipTests` flag to skip run of tests
    2. Add `-DparallelTests=false` flag to force tests to run sequentially. \
       Property `parallelTests` is set to `true` by
       default in [pom.xml](pom.xml). \
       Additionaly, [junit-platform.properties](src%2Ftest%2Fresources%2Fjunit-platform.properties) also
       have `junit.jupiter.execution.parallel.enabled=true`.
2. Execute jar package  
   `java -jar ./target/ForeignExchangeApiSpringObserver_complete_standalone.jar`
3. Endpoints are listed in the console after running the application

### Application design

#### Overview

At the center of application stands Observer pattern (also called Publisher/Subscriber). \
I chose to use **Spring**'s `ApplicationEventPublisher` implementation of this pattern.

Application consists of 2 main parts:

1. Publisher
    1. [PublisherController.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fcontroller%2FPublisherController.java) -
       receives CSV data with newest currency rates.
        1. Has 2 endpoints with same functionality but for different in/out data format: `APPLICATION/JSON`
           and `TEXT/PLAIN`
    2. [PublisherService.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fservice%2Fpublisher%2Fobservable%2Fsubject%2FPublisherService.java) -
       publishes CSV data in String form received by Publisher Controller.
2. Subscriber
    1. [SubscriberController.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fcontroller%2FSubscriberController.java) -
       serves data about currency rates for given currency. Currency is specified using path
       parameter in format `currency_code_1-currency_code_2`.
    2. [PublisherService.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fservice%2Fpublisher%2Fobservable%2Fsubject%2FPublisherService.java) -
       when asked by Subscriber Controller serves data about currency rate for given currency. This
       data is stored in-memory in a Map. Population of this data is triggered by `ApplicationEvent` (source of this
       event is Publisher Service).
        1. [ISubscriber.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fservice%2Fsubscriber%2Fobserver%2FISubscriber.java) -
           is an interface requiring implementation of `onMessage(<T> message)` method mentioned in description of the
           recruitment task.
        2. [ISubscriberService.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fservice%2Fsubscriber%2Fobserver%2FISubscriberService.java) -
           is an interface guaranteeing that any implementation of this interface will retrieve latest currency rete.

#### Further comments

Package `axal25.oles.jacek.dependency.external` simulates external dependency. In this package there are 2
classes: [ExternalRequestContractDto.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fdependency%2Fexternal%2FExternalRequestContractDto.java), [ExternalResponseContractDto.java](src%2Fmain%2Fjava%2Faxal25%2Foles%2Fjacek%2Fdependency%2Fexternal%2FExternalResponseContractDto.java). \
They simulate some kind of contract with the external service feeding our application information in CSV format about
currency
trade rates.


