package com.auth.domain.model

import com.auth.domain.exceptions.EmailException
import com.auth.domain.exceptions.PasswordException
import org.junit.Assert
import org.junit.Test


class LoginWithEmailAndPasswordTest {

    @Test
    fun createLoginAndEmailObject_success() {
        //Arrange
        val email = "jmerino1204@gmail.com"
        val password = "1234566"

        //Act
        val result = LoginWithEmailAndPassword(email, password)

        //Assert
        Assert.assertEquals(result.email, email)
        Assert.assertEquals(result.password, password)
    }

    @Test
    fun createLoginAndEmailObject_throwEmail() {
        //Arrange
        val email = ""
        val password = "1234566"

        //Assert
        Assert.assertThrows(EmailException::class.java) {
            //Act
            LoginWithEmailAndPassword(email, password)
        }
    }

    @Test
    fun createLoginAndEmailObject_incorrectEmailWithOutAt_throwEmail() {
        //Arrange
        val email = "jmerino1204.com"
        val password = "1234566"

        //Assert
        Assert.assertThrows(EmailException::class.java) {
            //Act
            LoginWithEmailAndPassword(email, password)
        }
    }

    @Test
    fun createLoginAndEmailObject_incorrectEmailWithOutDot_throwEmail() {
        //Arrange
        val email = "jmerino1204@com"
        val password = "1234566"

        //Assert
        Assert.assertThrows(EmailException::class.java) {
            //Act
            LoginWithEmailAndPassword(email, password)
        }
    }

    @Test
    fun createLoginAndEmailObject_incorrectEmailWithOutDotCom_throwEmail() {
        //Arrange
        val email = "jmerino1204@"
        val password = "1234566"

        //Assert
        Assert.assertThrows(EmailException::class.java) {
            //Act
            LoginWithEmailAndPassword(email, password)
        }
    }
    @Test
    fun createLoginAndEmailObject_incorrectEmailWithoutProvider_throwEmail() {
        //Arrange
        val email = "jmerino1204@.com"
        val password = "1234566"

        //Assert
        Assert.assertThrows(EmailException::class.java) {
            //Act
            LoginWithEmailAndPassword(email, password)
        }
    }

    @Test(expected = PasswordException::class)
    fun createLoginAndEmailObject_throwPassword() {
        //Arrange
        val email = "jmerino1204@gmail.com"
        val password = ""

        //Act
        LoginWithEmailAndPassword(email, password)

        //Assert
        /*PasswordException*/
    }

}