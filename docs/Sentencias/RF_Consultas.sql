-- Requerimientos Funcionales de Consulta
-- Iteracion 2
-- Grupo D-06

-- RFC1: Mostrar el dinero recibido por cada proveedor durante el año actual y transcurrido
SELECT PP.*, ASW.ID_PROPUESTA, asw."TOTAL GANANCIAS" FROM (

SELECT RE.ID_PROPUESTA AS "ID_PROPUESTA", SUM(RE.COSTO_TOTAL) AS "TOTAL GANANCIAS"
FROM RESERVAS RE
WHERE RE.ID_PROPUESTA IN (
    SELECT PT.ID
    FROM PROPUESTAS PT 
    WHERE PT.ID_PERSONA IN (
        SELECT PEP.ID  
        FROM PERSONAS PEP 
        WHERE ROL = 'operador'
    )
)
GROUP BY RE.ID_PROPUESTA
) ASW
INNER JOIN PROPUESTAS PU
ON PU.ID = ASW.ID_PROPUESTA

INNER JOIN PERSONAS PP
ON PP.ID = PU.ID_PERSONA

ORDER BY asw."TOTAL GANANCIAS" DESC

;


-- RFC2: Mostrar las 20 ofertas más populares
SELECT  *
FROM 
( SELECT ID_PROPUESTA, COUNT(ID_PROPUESTA) AS "Cantidad Reservas"  
FROM RESERVAS 
GROUP BY ID_PROPUESTA
ORDER BY "Cantidad Reservas" DESC)
WHERE ROWNUM <= 20;

-- RFC3: Indice de Ocupacion de cada Oferta

SELECT  * 
FROM  
SELECT ID_PROPUESTA, AVG(PERSONAS)  
FROM RESERVA
GROUP BY ID_PROPUESTA

-- RFC4: Alojamientos disponibles en un rango de fechas


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



