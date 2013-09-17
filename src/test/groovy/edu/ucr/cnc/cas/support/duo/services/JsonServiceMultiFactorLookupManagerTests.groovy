package edu.ucr.cnc.cas.support.duo.services

import spock.lang.Specification
import net.unicon.cas.addons.serviceregistry.RegisteredServiceWithAttributesImpl

class JsonServiceMultiFactorLookupManagerTests extends Specification {

  def "Access a service that does not have a MFA value"(){
      given:
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-NoMFASetting"
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == false
    }
  def "Access a service that requires MFA for all users"(){
      given:
        def multiFactorRequiredAttributeName = "casMFARequired"
        def multiFactorRequiredAttributeValue = "ALL"
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-MFARequired"
        service.extraAttributes = [(multiFactorRequiredAttributeName) : multiFactorRequiredAttributeValue]
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == true
    }

    def "Access a service that does not require MFA"(){
      given:
        def multiFactorRequiredAttributeName = "casMFARequired"
        def multiFactorRequiredAttributeValue = "NONE"
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-NoMFARequired"
        service.extraAttributes = [(multiFactorRequiredAttributeName) : multiFactorRequiredAttributeValue]
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == false
    }

  def "Access a service that requires MFA for some users"(){
      given:
        def multiFactorRequiredAttributeName = "casMFARequired"
        def multiFactorRequiredAttributeValue = "CHECK_LIST"
        def multiFactorRequiredUserListAttributeName = "casMFARequiredUsers"
        def multiFactorRequiredUserListAttributeValue = ["testUser","foo","bar"]
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-MFARequiredList"
        service.extraAttributes = [ (multiFactorRequiredAttributeName) : multiFactorRequiredAttributeValue,
                                    (multiFactorRequiredUserListAttributeName) : multiFactorRequiredUserListAttributeValue]
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == true
    }

  def "Access a service that requires MFA for some users - alternate attribute names"(){
      given:
        def multiFactorRequiredAttributeName = "requireTwoFactor"
        def multiFactorRequiredAttributeValue = "CHECK_LIST"
        def multiFactorRequiredUserListAttributeName = "TwoFactorUsers"
        def multiFactorRequiredUserListAttributeValue = ["testUser","foo","bar"]
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-MFARequiredList-AltNames"
        service.extraAttributes = [ (multiFactorRequiredAttributeName) : multiFactorRequiredAttributeValue,
                                    (multiFactorRequiredUserListAttributeName) : multiFactorRequiredUserListAttributeValue]
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        lookupManager.setMultiFactorRequiredAttributeName(multiFactorRequiredAttributeName)
        lookupManager.setMultiFactorRequiredUserListAttributeName(multiFactorRequiredUserListAttributeName)
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == true
    }

  def "Access a service that requires MFA for some users and the current user is not in the list"(){
      given:
        def multiFactorRequiredAttributeName = "casMFARequired"
        def multiFactorRequiredAttributeValue = "CHECK_LIST"
        def multiFactorRequiredUserListAttributeName = "casMFARequiredUsers"
        def multiFactorRequiredUserListAttributeValue = ["baz","foo","bar"]
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-MFARequiredList-NotRequired"
        service.extraAttributes = [ (multiFactorRequiredAttributeName) : multiFactorRequiredAttributeValue,
                                    (multiFactorRequiredUserListAttributeName) : multiFactorRequiredUserListAttributeValue]
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == false
    }

  def "Access a service that has an invalid requirement value"(){
      given:
        def multiFactorRequiredAttributeName = "casMFARequired"
        def multiFactorRequiredAttributeValue = "YES"
        def multiFactorRequiredUserListAttributeName = "casMFARequiredUsers"
        def multiFactorRequiredUserListAttributeValue = ["baz","foo","bar"]
        def username = "testUser"

        def service = new RegisteredServiceWithAttributesImpl()
        service.serviceId = "Test-InvalidMFA"
        service.extraAttributes = [ (multiFactorRequiredAttributeName) : multiFactorRequiredAttributeValue,
                                    (multiFactorRequiredUserListAttributeName) : multiFactorRequiredUserListAttributeValue]
      
      when:
        def lookupManager = new JsonServiceMultiFactorLookupManager()
        def result = lookupManager.getMFARequired(service,username)

      then:
        result == false
    }
}