# SOA
Projekty z przedmiotu: SOA w projektowaniu i implementacji oprogramowania.


# HOW TO RUN

# lab1:

prerequisites:
wildfly (16.0.0 or better)
maven (run on 3.6 version)

To prepare authorization:
Add these lines to standalone.xml file in wildfly directory:

<security-domain name="my-sec-domain" cache-type="default">
  <authentication>  
    <login-module code="UsersRoles" flag="required">
      <module-option name="usersProperties" value="users.properties"/>
      <module-option name="rolesProperties" value="roles.properties"/>
    </login-module>
  </authentication>
<security-domain/>
  
1. Run wildfly: sudo systemctl start wildfly
2. Deploy Service: mvn clean install wildfly:deploy

endpoint here: http://localhost:8080/soap-api/StudentServer?wsdl
