package io.datanerd.securemsg.service

import io.datanerd.securemsg.ServiceSpec
import org.assertj.core.api.Assertions

class EncryptionTest extends ServiceSpec {

  val origValue = "hello scala"
  behavior of "EncryptionTest"

  it should "encrypt data to string" in {
    val encyptedValue = Encryption.encrypt("key", origValue)
    Assertions.assertThat(encyptedValue).isNotNull
  }

  it should "decrypt data back to orig value" in {
    val encyptedValue = Encryption.encrypt("key", origValue)
    Assertions.assertThat(Encryption.decrypt("key", encyptedValue)).isEqualTo(origValue)
  }

}
