# scalapb-mate dev tips

## how to see sbt project dependencies
* see https://github.com/jrudolph/sbt-dependency-graph

## how to generate scala test report 
* see http://www.scalatest.org/user_guide/using_the_runner

```
testOptions in Test ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
      Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
    )
``` 

## how to use docker image to test scala code
* see https://github.com/codingwell/scala-guice
* see [DaoSpec](https://github.com/email2liyang/scalapb-mate/blob/master/message-service/src/test/scala/io/datanerd/securemsg/DaoSpec.scala) for details


## how to use user defined annotation in scala guice
* we have to define it in java then use the java annotation in scala class
* I know scala has some other DI framework, but Guice is my favourite, just personal taste.

## how to implement loan-fixture methods in ScalaTest
* see [MessageServiceImplTest](https://github.com/email2liyang/scalapb-mate/blob/master/message-service/src/test/scala/io/datanerd/securemsg/service/MessageServiceImplTest.scala) for details

## how to assert expected exception in ScataTest
```

  it should "report error when dao return none" in withParams { (stub, messageDao) =>
    Mockito.when(messageDao.getMessage(org.mockito.ArgumentMatchers.anyString()))
      .thenReturn(Future.successful(None))
    assertThrows[StatusRuntimeException] {
      stub.getMessage(new MessageId().withMessageId(faker.numerify("id-#")))
    }
  }
```

## how to override config value in unit test
* use [PowerConfig](https://github.com/email2liyang/scalapb-mate/blob/master/message-service/src/main/scala/io/datanerd/securemsg/guice/PowerConfig.scala)


