SELECT  PER.*,p.* , r.*
FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID = R.ID_PROPUESTA) INNER JOIN PERSONA PER ON R.ID_PERSONA = PER.ID
WHERE r.fecha_registro >= to_date('01-01-2014', 'DD-MM-YYYY') and R.FECHA_REGISTRO <= TO_DATE('31-12-2018', 'DD-MM-YYYY') AND PER.PAPEL = 'CLIENTE'
AND P.ID = 2
AND p.TIPO_INMUEBLE LIKE 'HOTEL'
AND per.TIPO LIKE '%PROFESORA%'
ORDER BY P.ID;



SELECT  PER.* , p.*
FROM PROPUESTA P INNER JOIN PERSONA PER ON P.ID_PERSONA = PER.ID
WHERE  NOT exists (SELECT RES.iD_PERSONA FROM RESERVA RES
                    WHERE PER.ID = RES.ID_PERSONA AND  rES.fecha_registro >= to_date('01-01-2014', 'DD-MM-YYYY')
                       and RES.FECHA_REGISTRO <= TO_DATE('31-12-2018', 'DD-MM-YYYY') 
                       AND PER.PAPEL = 'CLIENTE')

    AND P.ID = 26
    AND p.TIPO_INMUEBLE LIKE 'HOSTAL'
    AND per.TIPO LIKE '%PADRE%'
ORDER BY P.ID;


SELECT P.*, PER.* , r.*
FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID = R.ID_PROPUESTA) INNER JOIN PERSONA PER ON R.ID_PERSONA = PER.ID
WHERE r.fecha_registro >= p_FECH1 and R.FECHA_REGISTRO <= p_FECH2 AND PER.PAPEL = 'CLIENTE'
AND PER.ID = P_IDENT
AND TIPO_INMUEBLE LIKE P_TIPOINM
AND TIPO LIKE P_TIPORELACION
ORDER BY P.ID;


dbms_output on


-----------------------------------------------------------  
select N_semana , sum(duracion) dd from temp_semanas where id_propuesta is not null 
group by n_semana order by dd desc

   CREATE  TABLE temp_semanas (
N_semana       varchar2(3),
fecha_inicial  date,
Fecha_fin      date,
fecha_inicio   date,
duracion       number,
id_propuesta   number,
id_persona     number) ;

drop table temp_semanas;
delete temp_semanas;
commit;
create index i_fecha     
on temp_semanas (fecha_inicial);
create index i_fechafin on temp_semanas (fecha_fin);

create index i_fechares on reserva (fecha_inicio);

select * from temp_semanas;

Create or replace procedure llenar_temp_semanas (p_aa_consulta number) as
 P_fecha_in_semana date;
 V_duracion number; --LINEA 50
 CSem varchar2(3);
 begin
    delete temp_semanas;
    commit;
    P_fecha_in_semana := to_date ('01/01/'|| to_char(p_aa_consulta),'dd/mm/yyyy');

    for i in 1..52 loop
        CSem := to_char (I,'00');
       DBMS_OUTPUT.PUT_LINE('semanas  ' || to_char (I,'00') || ' ' || to_char(P_fecha_in_semana,'yyyy-mm-dd') || ' - ' || to_char(P_fecha_in_semana +6,'yyyy-mm-dd'));
         Insert into temp_semanas values (Csem,P_fecha_in_semana,P_fecha_in_semana +6, null,0,null,null);
       p_fecha_in_semana := P_fecha_in_semana + 7;    
    end loop;      
    commit;
 END;
    
begin
    llenar_temp_semanas (2018);
end;

--------------------------------------------------------------------

select * from reserva order by  id_propuesta, fecha_inicio
update reserva
set duracion = fecha_final-fecha_inicio +1;

--RFC 12

select lista.semana ,lista.propu , lista.dura
from 
(
SELECT TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA,r.id_propuesta as propu,  sum(r.duracion) as Dura
FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA)
GROUP BY R.ID_PROPUESTA , TO_CHAR(r.fecha_inicio, 'WW')
order by TO_CHAR(r.fecha_inicio, 'WW') 
) lista  
where lista.dura = (select   max(res.duracion)
                    FROM PROPUESTA Prop INNER JOIN RESERVA Res ON Prop.ID= Res.ID_PROPUESTA
                            where TO_CHAR(res.fecha_inicio, 'WW') = lista.semana
                            GROUP by TO_CHAR(res.fecha_inicio, 'WW')
                                 )

order by lista.semana

--RFC12 (2)
select lista.semana ,lista.propu , lista.dura
from 
(
SELECT TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA,r.id_propuesta as propu,  sum(r.duracion) as Dura
FROM PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA
GROUP BY R.ID_PROPUESTA , TO_CHAR(r.fecha_inicio, 'WW')
order by TO_CHAR(r.fecha_inicio, 'WW') 
) lista  
where lista.dura = (select   min(res.duracion)
                    FROM PROPUESTA Prop INNER JOIN RESERVA Res ON Prop.ID= Res.ID_PROPUESTA
                            where TO_CHAR(res.fecha_inicio, 'WW') = lista.semana
                            GROUP by TO_CHAR(res.fecha_inicio, 'WW')
                                 ) 

order by lista.semana

--RFC12 (3)

select lista.semana ,lista.OPERADOR , lista.CANT
from 
(
SELECT TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA,PER.ID as OPERADOR,  COUNT(r.duracion) as CANT
FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA) INNER JOIN PERSONA PER ON P.ID_PERSONA = PER.ID
GROUP BY PER.ID , TO_CHAR(r.fecha_inicio, 'WW')
order by TO_CHAR(r.fecha_inicio, 'WW') 
) lista  
where lista.CANT = (select   MAX(COUNT(*))
                    FROM (PROPUESTA Prop INNER JOIN RESERVA Res ON Prop.ID= Res.ID_PROPUESTA) INNER JOIN PERSONA OPER ON PROP.ID_PERSONA= OPER.ID
                            where TO_CHAR(res.fecha_inicio, 'WW') = lista.semana
                            GROUP by TO_CHAR(res.fecha_inicio, 'WW')
                                 ) 

order by lista.semana


--RFC12 (4)

select lista.semana ,lista.OPERADOR , lista.CANT
from 
(
SELECT TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA,PER.ID as OPERADOR,  COUNT(r.duracion) as CANT
FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA) INNER JOIN PERSONA PER ON P.ID_PERSONA = PER.ID
GROUP BY PER.ID , TO_CHAR(r.fecha_inicio, 'WW')
order by TO_CHAR(r.fecha_inicio, 'WW') 
) lista  
where lista.CANT = (select   MIN(COUNT(*))
                    FROM (PROPUESTA Prop INNER JOIN RESERVA Res ON Prop.ID= Res.ID_PROPUESTA) INNER JOIN PERSONA OPER ON PROP.ID_PERSONA= OPER.ID
                            where TO_CHAR(res.fecha_inicio, 'WW') = lista.semana
                            GROUP by TO_CHAR(res.fecha_inicio, 'WW')
                                 ) 

order by lista.semana

--------------------------------------------- fin 
select TO_CHAR(sysdate, 'WW')  from dual

select semana , prop1, max(dur1 )

from (

    SELECT  TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA, r.id_propuesta as Prop1,  sum(r.duracion) as Dur1
    -- ,max(r.duracion) , count (*)
    FROM RESERVA R
    GROUP  by TO_CHAR(r.fecha_inicio, 'WW'), r.id_propuesta
    order by   TO_CHAR(r.fecha_inicio, 'WW') asc, sum(duracion) desc
    )  
    order by semana

 
 
 
--LINEA 100





--RFC 9   
SELECT R.ID_PROPUESTA, SUM(R.DURACION)
FROM PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA
GROUP BY R.ID_PROPUESTA HAVING SUM(R.DURACION) <31;

*

select * from reserva 

 update reserva
 set duracion = 5
 where id=0
 



--RFC7

SELECT R.FECHA_INICIO, COUNT(R.FECHA_INICIO) 
FROM RESERVA R
WHERE FECHA_INICIO =< $FECHA AND FECHA_INICIO >= $FECH
ORDER BY FECHA_INICIO DESC




--RFC13
SELECT DISTINCT P.*
FROM (PERSONA P INNER JOIN RESERVA R ON P.ID = R.ID_PERSONA), PROPUESTA PROP, HOTEL H
WHERE (PROP.ID= R.ID_PROPUESTA AND PROP.ID_HOTEL= H.ID AND  H.TIPO_HABITACION ='Suite' ) OR ((R.COSTO/R.DURACION) >= 150) OR (P.ID IN (
SELECT DISTINCT PER.ID 
FROM PERSONA PER INNER JOIN RESERVA RES ON PER.ID = RES.ID_PERSONA
WHERE  (SYSDATE -  RES.FECHA_INICIO) < &MES * 30
GROUP BY PER.ID
HAVING COUNT ( PER.ID) >= &MES 
))


TO_NUMBER( TO_CHAR(SYSDATE, 'MM') - TO_CHAR(R.FECHA_INICIO, 'MM') )



SELECT TO_CHAR(SYSDATE,'MM') -  TO_CHAR(TO_DATE('01/03/2018','DD/MM/yyyy'), 'MM')FROM DUAL


CREATE INDEX I_FECHA_REG ON RESERVA (FECHA_REGISTRO);
CREATE INDEX I_TIPO_INMUEBLE ON PROPUESTA(TIPO_INMUEBLE);
CREATE INDEX I_COSTO ON RESERVA(COSTO);
CREATE INDEX I_DURACION ON RESERVA(DURACION);

commit;

