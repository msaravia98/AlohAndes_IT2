-- Requerimientos Funcionales de Consulta
-- Iteracion 2
-- Grupo D-06

-- RFC1: Mostrar el dinero recibido por cada proveedor durante el año actual y transcurrido
SELECT PP.*, ASW.ID_PROPUESTA, asw."TOTAL GANANCIAS" FROM (

SELECT RE.ID_PROPUESTA AS "ID_PROPUESTA", SUM(RE.COSTO) AS "TOTAL GANANCIAS"
FROM RESERVA RE
WHERE RE.ID_PROPUESTA IN (
    SELECT PT.ID
    FROM PROPUESTA PT 
    WHERE PT.ID_PERSONA IN (
        SELECT PEP.ID  
        FROM PERSONA PEP 
        WHERE PAPEL = 'OPERADOR'
    )
)
GROUP BY RE.ID_PROPUESTA
) ASW
INNER JOIN PROPUESTA PU
ON PU.ID = ASW.ID_PROPUESTA

INNER JOIN PERSONA PP
ON PP.ID = PU.ID_PERSONA

ORDER BY asw."TOTAL GANANCIAS" DESC;


-- RFC2: Mostrar las 20 ofertas más populares
SELECT  *
FROM 
( SELECT ID_PROPUESTA, COUNT(ID_PROPUESTA) AS "Cantidad Reservas"  
FROM RESERVA 
GROUP BY ID_PROPUESTA
ORDER BY "Cantidad Reservas" DESC)
WHERE ROWNUM <= 20;

-- RFC3: Indice de Ocupacion de cada Oferta

SELECT P.ID,(SUM(R.PERSONAS)/ (SUM(P.CAPACIDAD)))*100 AS PORCENTAJE 
FROM PROPUESTA P
INNER JOIN RESERVA R 
ON P.ID = R.ID_PROPUESTA 
GROUP BY P.ID;

-- RFC4: Alojamientos disponibles en un rango de fechas
SELECT ID_PROPUESTA  
FROM RESERVA  
WHERE TO_DATE(, 'yyyy/mm/dd') BETWEEN R.FECHA_INICIO AND R.FECHA_FINAL)
OR TO_DATE(, 'yyyy/mm/dd') BETWEEN R.FECHA_INICIO AND R.FECHA_FINAL)
-- RFC5: Mostrar uso alohAndes para cada tipo de miembro de la comunidad

SELECT TIPO, PAPEL, COSTOTOTAL,TOTALDIAS FROM PERSONA P
INNER JOIN
(SELECT R.ID_PERSONA AS AA, SUM(R.COSTO) AS COSTOTOTAL, SUM(R.DURACION) AS TOTALDIAS
FROM RESERVA R 
JOIN PERSONA P ON R.ID_PERSONA = P.ID
GROUP BY R.ID_PERSONA)X
ON P.ID = X.AA;


-- RFC6: Uso de alohAndes para un usuario dado
SELECT TIPO, COSTOTOTAL, TOTALDIAS FROM PERSONA P
INNER JOIN
(SELECT R.ID_PERSONA AS AA, SUM(R.COSTO) AS COSTOTOTAL, SUM(R.DURACION) AS TOTALDIAS
FROM RESERVA R 
JOIN PERSONA P ON R.ID_PERSONA = +ID_PERSONA+
GROUP BY R.ID_PERSONA)X
ON P.ID = X.AA;


-- RFC7: Analizar Operación AlohAndes

-- RCF8: Dar cliente más frecuente dado un tipo de inmueble
SELECT ID_PERSONA, COUNT(ID_PERSONA)
FROM(SELECT ID_PERSONA 
     FROM RESERVA RE 
     WHERE RE.ID_PROPUESTA 
     IN 
     (SELECT ID 
      FROM PROPUESTA 
      WHERE TIPO_INMUEBLE = UPPER("+tipoInmueble+")
     )
    ) 
GROUP BY ID_PERSONA
HAVING COUNT (ID_PERSONA) >3;

-- RFC9: Ofertas de Alojamiento sin demanda

SELECT ID, TIPO_INMUEBLE 
FROM PROPUESTA 
INNER JOIN RESERVA
ON  PROPUESTA.ID = RESERVA.ID_PROPUESTA
WHERE RESERVA.DURACION <= 30



