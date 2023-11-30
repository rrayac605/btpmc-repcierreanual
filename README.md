#Introducción
Microservicio encargado de procesar reportes de cierre anual

#Variables de entorno

============== AMBIENTE DE QA ==============

Para que este microservicio pueda ser ejecutado deberán de existir las siguietnes variables de entorno en el SO annfitrión donde se ejecute el jar

- portrepcierreanualBatch 9017
- authenticationdatabaseMongo PMCQA01
- usrMongoCifras pmcmsconsulta
- pwMongoCifras pmcmsconsulta0
- databaseMongo PMCQA01
- portMongo 27017
- hostMongo 10.100.8.78
- oracleUrl jdbc:oracle:thin:@zaprac2prd.imss.gob.mx:1630/serdg01
- oracleUsername MGPBDTU1
- oraclePassword T3mp0ra1
- fileReportes /pmcarchivos/cierre_anual
- fileLogBatchrepcierreanual /weblogic/pmc/logs/btpmc-repcierreanual.log
- cron.expression.repcierreanualBatch -> 0 0 1 * * ?
- cron.expression.repcierreanualBatchCleaner -> 0 0 23 * * ?
- cron.expression.repcierreanualBatchRestart -> 0 0 0 * * ?

============== AMBIENTE DE UAT ==============

Para que este microservicio pueda ser ejecutado deberán de existir las siguietnes variables de entorno en el SO annfitrión donde se ejecute el jar

- portrepcierreanualBatch 9017
- authenticationdatabaseMongo PMCUAT01
- usrMongoCifras pmcmsconsulta
- pwMongoCifras pmcmsconsulta0
- databaseMongo PMCUAT01
- portMongo 27017
- hostMongo 10.100.8.80
- oracleUrl jdbc:oracle:thin:@zaprac2prd.imss.gob.mx:1630/serdg01
- oracleUsername MGPBDTU1
- oraclePassword T3mp0ra1
- fileReportes /pmcarchivos/cierre_anual
- fileLogBatchrepcierreanual /weblogic/pmc/logs/btpmc-repcierreanual.log
- cron.expression.repcierreanualBatch -> 0 0 1 * * ?
- cron.expression.repcierreanualBatchCleaner -> 0 0 23 * * ?
- cron.expression.repcierreanualBatchRestart -> 0 0 0 * * ?

============== AMBIENTE DE PROD ==============

Para que este microservicio pueda ser ejecutado deberán de existir las siguietnes variables de entorno en el SO annfitrión donde se ejecute el jar

- portrepcierreanualBatch -> 9017
- authenticationdatabaseMongo -> PMC
- usrMongoCifras -> pmcmsconsulta
- pwMongoCifras -> pmcmsconsulta0
- databaseMongo -> PMC
- portMongo -> 27017
- hostMongo -> 10.100.6.172
- oracleUrl -> jdbc:oracle:thin:@172.16.8.138:1630/serdg01
- oracleUsername -> BDTU_PMC_LECTURA
- oraclePassword -> I3msst069#90
- fileReportes /pmcarchivos/cierre_anual
- fileLogBatchrepcierreanual -> /weblogic/pmc/logs/btpmc-repcierreanual.log
- cron.expression.repcierreanualBatch -> 0 0 1 * * ?
- cron.expression.repcierreanualBatchCleaner -> 0 0 23 * * ?
- cron.expression.repcierreanualBatchRestart -> 0 0 0 * * ?
