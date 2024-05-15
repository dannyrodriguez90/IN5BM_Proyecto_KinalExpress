DROP DATABASE IF EXISTS DBKinalSpressIN5BM;

CREATE DATABASE DBKinalSpressIN5BM;

USE DBKinalSpressIN5BM;

SET GLOBAL time_zone = '-6:00';

CREATE TABLE Clientes (
    clienteId INT NOT NULL,
    NIT VARCHAR(10) NOT NULL,
    nombreCliente VARCHAR(50) NOT NULL,
    apellidoCliente VARCHAR(50) NOT NULL,
    direccionCliente VARCHAR(150) NOT NULL,
    telefonoCliente VARCHAR(8) NOT NULL,
    correoCliente VARCHAR(45) NOT NULL,
    PRIMARY KEY PKclienteId (clienteId)
);

CREATE TABLE Proveedores(
    codigoProveedor INT NOT NULL,
    NITProveedor VARCHAR(10) NOT NULL,
    nombreProveedor VARCHAR(60) NOT NULL,
    apellidoProveedor VARCHAR(60) NOT NULL,
    direccionProveedor VARCHAR(150),
    razonSocial VARCHAR(60),
    contactoPrincipal VARCHAR(100),
    paginaWeb VARCHAR (50),
    PRIMARY KEY PK_codigoProveedor(codigoProveedor)
);

CREATE TABLE TipoDeProducto(
    codigoTipoProducto INT NOT NULL,
    descripcion VARCHAR(45) NOT NULL,
    PRIMARY KEY PK_codigoTipoDeProducto(codigoTipoProducto)
);

CREATE TABLE Compras (
    numeroDocumento INT NOT NULL,
    fechaDocumento DATE NOT NULL,
    descripcion VARCHAR(60) NOT NULL,
    totalDocumento DECIMAL(10,2) NOT NULL,
    PRIMARY KEY PK_numeroDocumento(numeroDocumento)
);

CREATE TABLE CargoEmpleado (
	codigoCargoEmpleado INT NOT NULL,
    nombreCargo VARCHAR (45),
    descripcionCargo VARCHAR (100),
    PRIMARY KEY PK_codigoCargoEmpleado(codigoCargoEmpleado)
);
----------------------------------------


------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarClientes (
    IN clienteId INT,
    IN NIT VARCHAR(10),
    IN nombreCliente VARCHAR(50),
    IN apellidoCliente VARCHAR(50),
    IN direccionCliente VARCHAR(150),
    IN telefonoCliente VARCHAR(8),
    IN correoCliente VARCHAR(45)
)
BEGIN
    INSERT INTO Clientes(clienteId, NIT, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
    VALUES (clienteId, NIT, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
END $$

DELIMITER ;

CALL sp_AgregarClientes(1, '1234567890', 'Juan', 'Pérez', '123 Main Street', '5551234', 'juanperez@example.com');
CALL sp_AgregarClientes(2, '0987654321', 'María', 'González', '456 Oak Avenue', '5555678', 'mariagonzalez@example.com');
CALL sp_AgregarClientes(3, '1357924680', 'Carlos', 'López', '789 Elm Street', '5559012', 'carloslopez@example.com');
CALL sp_AgregarClientes(4, '2468013579', 'Laura', 'Martínez', '321 Pine Street', '5553456', 'lauramartinez@example.com');
CALL sp_AgregarClientes(5, '9876543210', 'Ana', 'Sánchez', '654 Cedar Avenue', '5557890', 'anasanchez@example.com');

DELIMITER $$

CREATE PROCEDURE sp_ListarClientes()
BEGIN
    SELECT
        C.clienteId,
        C.NIT,
        C.nombreCliente,
        C.apellidoCliente,
        C.direccionCliente,
        C.telefonoCliente,
        C.correoCliente
    FROM
        Clientes C;
END $$

DELIMITER ;

CALL sp_ListarClientes();

DELIMITER $$

CREATE PROCEDURE sp_buscarClientes()
BEGIN
    SELECT
        Clientes.clienteId AS 'ID de Cliente:',
        Clientes.nombreCliente AS 'Nombre de Cliente:',
        Clientes.telefonoCliente AS 'Telefono de Cliente:'
    FROM
        Clientes
    WHERE
        Clientes.clienteId = clienteId;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_editarClientes(
    IN clienteId INT,
    IN NIT VARCHAR(10),
    IN nombreCliente VARCHAR(50),
    IN apellidoCliente VARCHAR(50),
    IN direccionCliente VARCHAR(150),
    IN telefonoCliente VARCHAR(8),
    IN correoCliente VARCHAR(45)
)
BEGIN
    UPDATE Clientes SET
        NIT = NIT,
        Clientes.nombreCliente = nombreCliente,
        Clientes.apellidoCliente = apellidoCliente,
        Clientes.direccionCliente = direccionCliente,
        Clientes.telefonoCliente = telefonoCliente,
        Clientes.correoCliente = correoCliente
    WHERE
        Clientes.clienteId = clienteId;
END $$

DELIMITER ;

CALL sp_editarClientes(1,'f','fh','hf','fhf','fhgfhf','fghfghfg');

DELIMITER $$

CREATE PROCEDURE sp_eliminarClientes(IN clienteId INT)
BEGIN
    DELETE FROM Clientes WHERE clienteId = clienteId;
END $$

DELIMITER ;

-------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_AgregarProveedor (
    IN codigoProveedor INT,
    IN NITProveedor VARCHAR(10),
    IN nombreProveedor VARCHAR(60),
    IN apellidoProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoPrincipal VARCHAR(100),
    IN paginaWeb VARCHAR(50)
)
BEGIN
    INSERT INTO Proveedores(codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
    VALUES (codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
END $$

DELIMITER ;

CALL sp_AgregarProveedor(1, 'NITodoFeo', 'Pancho', 'Villa', 'Pollo Campero atrasito', 'olaaa', 'shi','como estas');
CALL sp_AgregarProveedor(2, 'NITodoFeo', 'Pancho', 'Villa', 'Pollo Campero atrasito', 'cooomooo', 'bien','olaaa');
CALL sp_AgregarProveedor(3, '1234567890', 'Juan', 'Pérez', '123 Main Street', 'Empresa A', 'Juan Pérez', 'www.empresaA.com');
CALL sp_AgregarProveedor(4, '0987654321', 'María', 'González', '456 Oak Avenue', 'Empresa B', 'María González', 'www.empresaB.com');
CALL sp_AgregarProveedor(5, '1357924680', 'Carlos', 'López', '789 Elm Street', 'Empresa C', 'Carlos López', 'www.empresaC.com');
CALL sp_AgregarProveedor(6, '2468013579', 'Laura', 'Martínez', '321 Pine Street', 'Empresa D', 'Laura Martínez', 'www.empresaD.com');
CALL sp_AgregarProveedor(7, '9876543210', 'Ana', 'Sánchez', '654 Cedar Avenue', 'Empresa E', 'Ana Sánchez', 'www.empresaE.com');

DELIMITER $$

CREATE PROCEDURE sp_ListarProveedor()
BEGIN
    SELECT
        C.codigoProveedor,
        C.NITProveedor,
        C.nombreProveedor,
        C.apellidoProveedor,
        C.direccionProveedor,
        C.razonSocial,
        C.contactoPrincipal,
        C.paginaWeb
    FROM
        Proveedores C;
END $$

DELIMITER ;

CALL sp_ListarProveedor();

DELIMITER $$

CREATE PROCEDURE sp_editarProveedor (
    IN codigoProveedor INT,
    IN NITProveedor VARCHAR(10),
    IN nombreProveedor VARCHAR(60),
    IN apellidoProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoPrincipal VARCHAR(100),
    IN paginaWeb VARCHAR(50)
)
BEGIN
    UPDATE Proveedores SET
        NITProveedor = NITProveedor,
        Proveedores.nombreProveedor = nombreProveedor,
        Proveedores.apellidoProveedor = apellidoProveedor,
        Proveedores.direccionProveedor = direccionProveedor,
        Proveedores.razonSocial = razonSocial,
        Proveedores.contactoPrincipal = contactoPrincipal,
        Proveedores.paginaWeb = paginaWeb
    WHERE
        Proveedores.codigoProveedor = codigoProveedor;
END $$

DELIMITER ;

CALL sp_editarProveedor(1,'viendo','con los ojos','omg','estas hablando','partido','omg','olaa');

DELIMITER $$

CREATE PROCEDURE sp_eliminarProveedor (codigoProveedor INT)
BEGIN
    DELETE FROM Proveedores WHERE codigoProveedor = codigoProveedor;
END $$

DELIMITER ;

------------------------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_AgregarTipoDeProducto (IN codigoTipoProducto INT, IN descripcion VARCHAR(45))
BEGIN
    INSERT INTO TipoDeProducto (codigoTipoProducto, descripcion)
    VALUES (codigoTipoProducto, descripcion);
END $$

DELIMITER ;
CALL sp_AgregarTipoDeProducto(1,'bien');
CALL sp_AgregarTipoDeProducto(2,'chido');



DELIMITER $$

CREATE PROCEDURE sp_ListarTipoDeProducto()
BEGIN
    SELECT
        C.codigoTipoProducto,
        C.descripcion
    FROM
        TipoDeProducto C;
END $$

DELIMITER ;

CALL sp_ListarTipoDeProducto();

DELIMITER $$

CREATE PROCEDURE sp_editarTipoDeProducto (IN codigoTipoProducto INT, IN descripcion VARCHAR(45))
BEGIN
    UPDATE TipoDeProducto SET
        TipoDeProducto.descripcion = descripcion
    WHERE
        TipoDeProducto.codigoTipoProducto = codigoTipoProducto;
END $$

DELIMITER ;

CALL sp_editarTipoDeProducto(8,'dinamico y mega realista');

DELIMITER $$

CREATE PROCEDURE sp_eliminarTipoDeProducto (codigoTipoProducto INT)
BEGIN
    DELETE FROM TipoDeProducto WHERE codigoTipoProducto = codigoTipoProducto;
END $$

DELIMITER ;
 
--------------------------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_AgregarCompras (
    IN numeroDocumento INT,
    IN fechaDocumento DATE,
    IN descripcion VARCHAR(60),
    IN totalDocumento DECIMAL(10,2)
)
BEGIN
    INSERT INTO Compras (numeroDocumento, fechaDocumento, descripcion, totalDocumento)
    VALUES (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
END $$

DELIMITER ;

CALL sp_AgregarCompras(1, '2007-09-23', 'biiig', 100.00);
CALL sp_AgregarCompras(2, '2007-02-24', 'BOOOOOOO', 50.00);
CALL sp_AgregarCompras(3, '2024-05-01', 'Suministros de oficina', 250.75);
CALL sp_AgregarCompras(4, '2024-04-15', 'Material de construcción', 1200.00);
CALL sp_AgregarCompras(5, '2024-05-05', 'Equipamiento informático', 1800.50);

DELIMITER $$

CREATE PROCEDURE sp_ListarCompras()
BEGIN
    SELECT
        C.numeroDocumento,
        C.fechaDocumento,
        C.descripcion,
        C.totalDocumento
    FROM
        Compras C;
END $$

DELIMITER ;

CALL sp_ListarCompras();

DELIMITER $$

CREATE PROCEDURE sp_editarCompras (
    IN numeroDocumento INT,
    IN fechaDocumento DATE,
    IN descripcion VARCHAR(60),
    IN totalDocumento DECIMAL(10,2)
)
BEGIN
    UPDATE Compras SET
        fechaDocumento = fechaDocumento,
        Compras.descripcion = descripcion,
        Compras.totalDocumento =  totalDocumento
    WHERE
        Compras.numeroDocumento = numeroDocumento;
END $$

DELIMITER ;

CALL sp_editarCompras(4,'2008-04-23','oooop', 58.00);

DELIMITER $$

CREATE PROCEDURE sp_eliminarCompras (numeroDocumento INT)
BEGIN
    DELETE FROM Compras WHERE numeroDocumento = numeroDocumento;
END $$

DELIMITER ;

-----------------------------------------------------------------------------------
DELIMITER $$

CREATE PROCEDURE sp_AgregarCargoEmpleado (
    IN codigoCargoEmpleado INT,
    IN nombreCargo VARCHAR(45),
    IN descripcionCargo VARCHAR(100)
)
BEGIN
    INSERT INTO CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
    VALUES (codigoCargoEmpleado, nombreCargo, descripcionCargo);
END $$

DELIMITER ;

CALL sp_AgregarCargoEmpleado(1, 'Danny', 'este empleado es la cabra');
CALL sp_AgregarCargoEmpleado(2, 'Messi', 'le huele mal los pies');
CALL sp_AgregarCargoEmpleado(3, 'Analista de Recursos Humanos','Encargado de reclutar, seleccionar y capacitar al personal.');
CALL sp_AgregarCargoEmpleado(4, 'Técnico de Soporte', 'Brinda asistencia técnica a los usuarios para resolver problemas con el hardware y software.');
CALL sp_AgregarCargoEmpleado(5, 'Diseñador Gráfico', 'Crea diseños gráficos para publicidad, sitios web y materiales de marketing.');

DELIMITER $$

CREATE PROCEDURE sp_ListarCargoEmpleado()
BEGIN
    SELECT
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    FROM
        CargoEmpleado;
END $$

DELIMITER ;

CALL sp_ListarCargoEmpleado();

DELIMITER $$

CREATE PROCEDURE sp_editarCargoEmpleado(
    IN codigoCargoEmpleado INT,
    IN nombreCargo VARCHAR(45),
    IN descripcionCargo VARCHAR(45)
)
BEGIN
    UPDATE CargoEmpleado SET
        CargoEmpleado.nombreCargo = nombreCargo,
        CargoEmpleado.descripcionCargo = descripcionCargo
    WHERE
        CargoEmpleado.codigoCargoEmpleado = codigoCargoEmpleado;
END $$

DELIMITER ;

CALL sp_editarCargoEmpleado(3,'Juan','Es bolo');

DELIMITER $$

CREATE PROCEDURE sp_eliminarCargoEmpleado(IN codigoCargoEmpleado INT)
BEGIN
    DELETE FROM CargoEmpleado WHERE codigoCargoEmpleado = codigoCargoEmpleado;
END $$

DELIMITER ;

