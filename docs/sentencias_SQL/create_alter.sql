-- 
-- 
-- CREAR
-- 
-- 


-- crea la tabla PERSONAS
CREATE TABLE PERSONAS (
    ID INT, 
    NOMBRE VARCHAR(25), 
    APELLIDO VARCHAR(25), 
    CEDULA VARCHAR(15),
    TIPO VARCHAR(25),
    ROL VARCHAR(25),
    NIT VARCHAR(25),
    EMAIL VARCHAR(25)
);

-- crea la tabla RESERVAS
CREATE TABLE RESERVAS ( 
    ID INT, 
    ID_PERSONA INT, 
    ID_PROPUESTA INT,
    FECHA_REGISTRO VARCHAR(25), 
    FECHA_CANCELACION VARCHAR(25), 
    FECHA_INICIO_ESTADIA VARCHAR(25), 
    DURACION_CONTRATO INT, 
    COSTO_TOTAL DOUBLE
);

-- crea la tabla PROPUESTAS
create table PROPUESTAS (
	ID INT,
	TIPO_INMUEBLE VARCHAR(25),
    ID_PERSONA INT,
	ID_HOTEL INT,
	ID_HOSTEL INT,
	ID_VIVIENDA_EXPRESS INT,
	ID_APARTAMENTO INT,
	ID_VIVIENDA_UNIVERSITARIA INT,
	ID_HABITACION INT,
    SE_VA_RETIRAR INT,
    HABILITADA INT
    
);

--crea tabla de normalizacion
create table PROPUESTA_DESHABILITADA (
	ID_PROPUESTA INT,
	FECHA_INICIO_DESHABILITADA VARCHAR(25),
    FECHA_FIN_DESHABILITADA VARCHAR(25)

);

-- crea la tabla HOTELES
CREATE TABLE HOTELES ( 
    ID INT, 
    REGISTRO_CAMARA_COMERCIO VARCHAR(500), 
    REGISTRO_SUPERINTENDENCIA VARCHAR(500), 
    TIPO_HABITACION VARCHAR(25), 
    HORARIO_ADMIN_24H NUMBER(1,0), 
    TAMANO VARCHAR(25), 
    UBICACION VARCHAR(50), 
    CANTIDAD_NOCHES INT 
);

-- crea la tabla HOSTELES
CREATE TABLE HOSTELES ( 
    ID INT, 
    REGISTRO_CAMARA_COMERCIO VARCHAR(500), 
    REGISTRO_SUPERINTENDENCIA VARCHAR(500),
    TIPO_HABITACION VARCHAR(25), 
    HORARIO_ADMIN_INICIAL INT, 
    HORARIO_ADMIN_FINAL INT, 
    TAMANO VARCHAR(25), 
    UBICACION VARCHAR(50), 
    CANTIDAD_NOCHES INT 
);


-- crea tabla HOTEL_TIENE_SERVICIOS
CREATE TABLE HOTEL_TIENE_SERVICIOS (
    ID_SERVICIO_BASICO_HOTELERO INT,
    ID_HOTEL INT, 
    ID_HOSTEL INT 
);

-- crea tabla SERVICIOS_BASICOS_HOTELEROS
CREATE TABLE SERVICIOS_BASICOS_HOTELEROS ( 
    ID INT, 
    NOMBRE VARCHAR(25)
);

-- crea tabla VIVIENDA_EXPRESS
CREATE TABLE VIVIENDAS_EXPRESS ( 
    ID INT, 
    NUMERO_HABITACIONES INT, 
    MENAJE VARCHAR(200) , 
    UBICACION VARCHAR(50) 
);

-- crea tabla APARTAMENTO
CREATE TABLE APARTAMENTOS ( 
    ID INT, 
    AMOBLADO NUMBER(1,0), 
    COSTO_ADMIN FLOAT 
);

-- crea tabla VIVIENDA_UNIVERITARIA
CREATE TABLE VIVIENDAS_UNIVERSITARIAS ( 
    ID INT, 
    UBICACION VARCHAR(50), 
    CAPACIDAD VARCHAR(25), 
    MENAJE VARCHAR(200), 
    TIPO_HABITACION VARCHAR(25), 
    MENSUAL NUMBER(1,0), 
    DESCRIPCION VARCHAR(200) 
);

-- crea tabla HABITACION 
CREATE TABLE HABITACIONES ( 
    ID INT, 
    PRECIO_ESPECIAL NUMBER(1,0), 
    TIPO_HABITACION VARCHAR(25) 
);

-- Crea tabla SERVICIOS_BASICOS
CREATE TABLE SERVICIOS_BASICOS ( 
    ID INT, 
    ID_TIPO INT, 
    ID_HABITACION INT, 
    ID_VIVIENDA_UNIVERSITARIA INT, 
    ID_APARTAMENTO INT, 
    ID_VIVIENDA_EXPRESS INT 
);

-- crea tabla TIPOS de servicio basico
CREATE TABLE TIPOS ( 
    ID INT, 
    NOMBRE VARCHAR(25), 
    COSTO DOUBLE 
);









-- 
-- 
-- ALTERAR
-- 
-- 


-- PERSONAS
ALTER TABLE PERSONAS ADD PRIMARY KEY (ID);
ALTER TABLE PERSONAS MODIFY NOMBRE VARCHAR(25) NOT NULL;
ALTER TABLE PERSONAS MODIFY APELLIDO VARCHAR(25) NOT NULL;
ALTER TABLE PERSONAS MODIFY TIPO VARCHAR(25) DEFAULT 'normal';
ALTER TABLE PERSONAS MODIFY ROL VARCHAR(25) DEFAULT 'cliente';
ALTER TABLE PERSONAS MODIFY NIT DEFAULT NULL;


-- HOTELES
ALTER TABLE HOTELES ADD PRIMARY KEY (ID);
ALTER TABLE HOTELES MODIFY REGISTRO_CAMARA_COMERCIO NOT NULL;
ALTER TABLE HOTELES MODIFY REGISTRO_SUPERINTENDENCIA NOT NULL;
ALTER TABLE HOTELES  add constraint TIPOS check (TIPO_HABITACION in ('estandar', 'semisuite', 'suite'));
ALTER TABLE HOTELES add constraint TAMANOS check (TAMANO in ('grande', 'mediano', 'pequeño'));

-- HOTELES
ALTER TABLE HOSTELES ADD PRIMARY KEY (ID);
ALTER TABLE HOSTELES MODIFY REGISTRO_CAMARA_COMERCIO TEXT NOT NULL;
ALTER TABLE HOSTELES MODIFY REGISTRO_SUPERINTENDENCIA TEXT NOT NULL;
ALTER TABLE HOSTELES  add constraint HORARIO_I_CHEK CHECK ( HORARIO_ADMIN_INICIAL >=  0 AND HORARIO_ADMIN_INICIAL <=  24);
ALTER TABLE HOSTELES ADD CONSTRAINT HORARIO_F_CHECK CHECK ( HORARIO_ADMIN_FINAL >=  0 AND HORARIO_ADMIN_FINAL <=  24);
ALTER TABLE HOSTELES  ADD CONSTRAINT  HORARIO_CHEK  CHECK ( HORARIO_ADMIN_INICIAL < HORARIO_ADMIN_FINAL );
ALTER TABLE HOSTELES  add constraint TIPO_HAB check (TIPO_HABITACION in ('compartida', 'sencilla'));

-- SERVICIOS_BASICOS_HOTELEROS
ALTER TABLE SERVICIOS_BASICOS_HOTELEROS ADD PRIMARY KEY (ID);

-- HOTEL_TIENE_SERVICIOS
ALTER TABLE HOTEL_TIENE_SERVICIOS ADD FOREIGN KEY (ID_HOTEL) REFERENCES HOTELES(ID);
ALTER TABLE HOTEL_TIENE_SERVICIOS ADD FOREIGN KEY (ID_HOSTEL) REFERENCES HOSTELES(ID);
ALTER TABLE HOTEL_TIENE_SERVICIOS ADD FOREIGN KEY (ID_SERVICIO_BASICO_HOTELERO) REFERENCES SERVICIOS_BASICOS_HOTELEROS(ID);

-- VIVIENDA_EXPRESS
ALTER TABLE VIVIENDAS_EXPRESS ADD PRIMARY KEY (ID);

-- APARTAMENTO
ALTER TABLE APARTAMENTOS ADD PRIMARY KEY (ID);

-- VIVIENDA_UNIVERSITARIA
ALTER TABLE VIVIENDAS_UNIVERSITARIAS ADD PRIMARY KEY (ID);

-- HABIACION 
ALTER TABLE HABITACIONES ADD PRIMARY KEY (ID);

-- TIPO
ALTER TABLE TIPOS ADD PRIMARY KEY (ID);
ALTER TABLE TIPOS MODIFY COSTO DEFAULT NULL;

-- SERVICIOS_BASICOS
ALTER TABLE SERVICIOS_BASICOS ADD PRIMARY KEY (ID);
ALTER TABLE SERVICIOS_BASICOS ADD FOREIGN KEY (ID_TIPO) REFERENCES TIPOS (ID);
ALTER TABLE SERVICIOS_BASICOS ADD FOREIGN KEY (ID_HABITACION) REFERENCES HABITACIONES (ID);
ALTER TABLE SERVICIOS_BASICOS ADD FOREIGN KEY (ID_VIVIENDA_UNIVERSITARIA) REFERENCES VIVIENDAS_UNIVERSITARIAS (ID);
ALTER TABLE SERVICIOS_BASICOS ADD FOREIGN KEY (ID_APARTAMENTO) REFERENCES APARTAMENTOS (ID);
ALTER TABLE SERVICIOS_BASICOS ADD FOREIGN KEY (ID_VIVIENDA_EXPRESS) REFERENCES VIVIENDAS_EXPRESS (ID);

-- PROPUESTAS
ALTER TABLE PROPUESTAS ADD PRIMARY KEY (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_PERSONA) REFERENCES PERSONAS (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_HOTEL) REFERENCES HOTELES (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_HOSTEL) REFERENCES HOSTELES (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_VIVIENDA_EXPRESS) REFERENCES VIVIENDAS_EXPRESS (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_APARTAMENTO) REFERENCES APARTAMENTOS (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_VIVIENDA_UNIVERSITARIA) REFERENCES VIVIENDAS_UNIVERSITARIAS (ID);
ALTER TABLE PROPUESTAS ADD FOREIGN KEY (ID_HABITACION) REFERENCES HABITACIONES (ID);

-- RESERVAS
ALTER TABLE RESERVAS ADD PRIMARY KEY (ID);
ALTER TABLE RESERVAS ADD FOREIGN KEY (ID_PERSONA) REFERENCES PERSONAS (ID);
ALTER TABLE RESERVAS MODIFY FECHA_CANCELACION VARCHAR(25) DEFAULT NULL;
ALTER TABLE RESERVAS ADD FOREIGN KEY (ID_PROPUESTA) REFERENCES PROPUESTAS (ID);
