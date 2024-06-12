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
    PRIMARY KEY PK_clienteId (clienteId)
);

CREATE TABLE Proveedores(
    codigoProveedor INT,
    NITProveedor VARCHAR(10),
    nombreProveedor VARCHAR(60),
    apellidoProveedor VARCHAR(60),
    direccionProveedor VARCHAR(150),
    razonSocial VARCHAR(60),
    contactoPrincipal VARCHAR(100),
    paginaWeb VARCHAR (50),
    PRIMARY KEY PK_codigoProveedor(codigoProveedor)
);

CREATE TABLE TipoDeProducto(
    codigoTipoProducto INT,
    descripcion VARCHAR(45),
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
    nombreCargo VARCHAR (45) NOT NULL,
    descripcionCargo VARCHAR (100) NOT NULL,
    PRIMARY KEY PK_codigoCargoEmpleado(codigoCargoEmpleado)
);


CREATE TABLE Productos(
	codigoProducto VARCHAR (45),
    descripcionProducto VARCHAR (45),
    precioUnitario DECIMAL (10,2),
    precioDocena DECIMAL (10,2),
    precioMayor DECIMAL (10,2),
    existencia INT, 
    codigoProveedor INT, 
    codigoTipoProducto INT,
    foreign key (codigoTipoProducto) references TipoDeProducto(codigoTipoProducto),
	foreign key (codigoProveedor) references Proveedores(codigoProveedor),
	PRIMARY KEY PK_codigoProducto (codigoProducto)
);


CREATE TABLE Empleados(
	codigoEmpleado INT NOT NULL,
    nombresEmpleado VARCHAR (50) NOT NULL,
    apellidosEmpleado VARCHAR (50) NOT NULL,
    sueldo DECIMAL (10,2) NOT NULL,
    direccion VARCHAR (150) NOT NULL,
    turno VARCHAR (15) NOT NULL,
    codigoCargoEmpleado INT NOT NULL,
    constraint FK_codigoCargoEmpleadoss foreign key CargoEmpleado(codigoCargoEmpleado)
	references CargoEmpleado(codigoCargoEmpleado),
	PRIMARY KEY PK_codigoEmpleado (codigoEmpleado)
);

CREATE TABLE EmailProveedor(
	codigoEmailProveedor INT NOT NULL,
    emailProveedor VARCHAR (50) NOT NULL,
    descripcion VARCHAR (100) NOT NULL,
    codigoProveedor INT NOT NULL,
	PRIMARY KEY PK_codigoEmailProveedores (codigoEmailProveedor),
    constraint FK_codigoProveedor foreign key Proveedores(codigoProveedor)
	references Proveedores(codigoProveedor)
);

CREATE TABLE TelefonoProveedor(
	codigoTelefonoProveedor INT NOT NULL,
    numeroPrincipal VARCHAR (8) NOT NULL,
    numeroSecundario VARCHAR (8),
    observaciones VARCHAR (45),
    codigoProveedor INT NOT NULL,
	PRIMARY KEY PK_codigoTelefonoProveedor (codigoTelefonoProveedor),
    constraint FK_codigoTelefonoProveedor foreign key Proveedores(codigoProveedor)
	references Proveedores(codigoProveedor)
);

CREATE TABLE DetalleCompra(
	codigoDetalleCompra INT NOT NULL,
    costoUnitario DECIMAL (10,2) NOT NULL,
    cantidad INT NOT NULL,
    codigoProducto VARCHAR (45),
    numeroDocumento INT,
    constraint FK_codigoProductos foreign key Productos (codigoProducto)
	references Productos(codigoProducto),
    constraint FK_numeroDocumentoss foreign key Compras(numeroDocumento)
	references Compras(numeroDocumento),
	PRIMARY KEY PK_codigoDetalleCompra (codigoDetalleCompra)
);

CREATE TABLE Factura(
	numeroFactura INT NOT NULL,
    estado VARCHAR (50) NOT NULL,
    totalFactura DECIMAL (10,2) NOT NULL,
    fechaFactura VARCHAR (45) NOT NULL,
    clienteId INT NOT NULL,
    codigoEmpleado INT NOT NULL,
	constraint FK_codigoClientes foreign key Clientes (clienteId)
	references Clientes(clienteId),
    constraint FK_codigoEmpleados foreign key Empleados(codigoEmpleado)
	references Empleados(codigoEmpleado),
	PRIMARY KEY PK_numeroFactura (numeroFactura)
);

CREATE TABLE DetalleFactura (
    codigoDetalleFactura INT NOT NULL,
    precioUnitario DECIMAL(10,2),
    cantidad INT NOT NULL,
    numeroFactura INT NOT NULL,
    codigoProducto VARCHAR(45) NOT NULL,
    CONSTRAINT FK_numeroFacturas FOREIGN KEY Factura (numeroFactura)
	REFERENCES Factura (numeroFactura),
    CONSTRAINT FK_codigoProducto_DetalleFacturas FOREIGN KEY Productos (codigoProducto)
	REFERENCES Productos (codigoProducto),
    PRIMARY KEY (codigoDetalleFactura)
);


ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'pitudo37d*';
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
CREATE PROCEDURE sp_buscarCliente(IN clienteId INT)
BEGIN
    SELECT
        clienteId,
        NIT,
        nombreCliente,
        apellidoCliente,
        direccionCliente,
        telefonoCliente,
        correoCliente
    FROM
        Clientes
    WHERE
        clienteId = clienteId;
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

CALL sp_editarClientes(2,'0987654321','María','González','123 Main Street','5555678','mariagonzalez@example.com');


Delimiter $$
	Create procedure sp_EliminarClientes (in cliID int)
		Begin 
			Delete from Clientes
				where clienteID = cliID;
		End $$
Delimiter ;


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
CALL sp_AgregarProveedor(1, '1234567890', 'Juan', 'Pérez', '123 Main Street', 'Empresa A', 'Juan Pérez', 'www.empresaA.com');
CALL sp_AgregarProveedor(2, '0987654321', 'María', 'González', '456 Oak Avenue', 'Empresa B', 'María González', 'www.empresaB.com');
CALL sp_AgregarProveedor(3, '1357924680', 'Carlos', 'López', '789 Elm Street', 'Empresa C', 'Carlos López', 'www.empresaC.com');
CALL sp_AgregarProveedor(4, '2468013579', 'Laura', 'Martínez', '321 Pine Street', 'Empresa D', 'Laura Martínez', 'www.empresaD.com');
CALL sp_AgregarProveedor(5, '9876543210', 'Ana', 'Sánchez', '654 Cedar Avenue', 'Empresa E', 'Ana Sánchez', 'www.empresaE.com');





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
CREATE PROCEDURE sp_buscarProveedor(IN _codigoProveedor INT)
BEGIN
    SELECT
        codigoProveedor,
        NITProveedor,
        nombreProveedor,
        apellidoProveedor,
        direccionProveedor,
        razonSocial,
        contactoPrincipal,
        paginaWeb
    FROM
        Proveedores
    WHERE
        codigoProveedor = _codigoProveedor;
END $$
DELIMITER ;


call sp_buscarProveedor(3);

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

CALL sp_editarProveedor(1, '1234567890', 'Danny', 'Rodriguez', '123 Main Street', 'Empresa A', 'Juan Pérez', 'www.empresaA.com');


Delimiter $$
	Create procedure sp_eliminarProveedor (in proveeID int)
		Begin 
			Delete from Proveedores
				where codigoProveedor = proveeID;
		End $$
Delimiter ; 


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
CALL sp_AgregarTipoDeProducto(3,'mal');
CALL sp_AgregarTipoDeProducto(4,'mas o menos');


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
CREATE PROCEDURE sp_buscarTipoDeProducto(IN codigoTipoProducto INT)
BEGIN
    SELECT
        codigoTipoProducto,
        descripcion
    FROM
        TipoDeProducto
    WHERE
        codigoTipoProducto = codigoTipoProducto;
END $$
DELIMITER ;


call sp_buscarTipoDeProducto(2);



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



Delimiter $$
	Create procedure sp_eliminarTipoDeProducto (in TipoProductoID int)
		Begin 
			Delete from TipoDeProducto
				where codigoTipoProducto = TipoProductoID;
		End $$
Delimiter ; 


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
    SELECT * FROM Compras;
END$$
DELIMITER ;


CALL sp_ListarCompras();

DELIMITER $$
CREATE PROCEDURE sp_buscarCompras(IN numeroDocumento INT)
BEGIN
    SELECT
        numeroDocumento,
        fechaDocumento,
        descripcion,
        totalDocumento
    FROM
        Compras
    WHERE
        numeroDocumento = numeroDocumento;
END $$
DELIMITER ;



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



Delimiter $$
	Create procedure sp_eliminarCompras (in compraID int)
		Begin 
			Delete from Compras
				where numeroDocumento = compraID;
		End $$
Delimiter ; 


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
CREATE PROCEDURE sp_buscarCargoEmpleado(IN codigoCargoEmpleado INT)
BEGIN
    SELECT
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    FROM
        CargoEmpleado
    WHERE
        codigoCargoEmpleado = codigoCargoEmpleado;
END $$
DELIMITER ;

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


Delimiter $$
	Create procedure sp_eliminarCargoEmpleado (in CargoEmpleadoID int)
		Begin 
			Delete from CargoEmpleado
				where codigoCargoEmpleado = CargoEmpleadoID;
		End $$
Delimiter ; 

-- CALL sp_eliminarCargoEmpleado(6);

---------------------------------------------------------

--------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarProducto (
    IN codigoProducto VARCHAR (45),
    IN descripcionProducto VARCHAR(45),
    IN precioUnitario DECIMAL(10,2),
    IN precioDocena DECIMAL(10,2),
    IN precioMayor DECIMAL(10,2),
    IN existencia INT,
    IN codigoProveedor INT,
    IN codigoTipoProducto INT
)
BEGIN
    INSERT INTO Productos(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoProveedor, codigoTipoProducto)
    VALUES (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoProveedor, codigoTipoProducto);
END $$
DELIMITER ;

CALL sp_AgregarProducto('1', 'Camisa', 25.50, 28.00, 60.00, 100, 2, 2);
CALL sp_AgregarProducto('2', 'Pantalón', 35.75, 39.00, 85.00, 80, 3, 3);
CALL sp_AgregarProducto('3', 'Zapatos', 50.00, 55.00, 12.00, 50, 4, 4);
CALL sp_AgregarProducto('4', 'Corbata', 15.25, 16.00, 35.00, 120, 1, 1);


DELIMITER $$
CREATE PROCEDURE sp_ListarProducto()
BEGIN
    SELECT * FROM Productos;
END$$
DELIMITER ;

CALL sp_ListarProducto();

DELIMITER $$
CREATE PROCEDURE sp_buscarProducto(IN codigoProducto VARCHAR (45))
BEGIN
    SELECT
        codigoProducto,
        descripcionProducto,
        precioUnitario,
        precioDocena,
        precioMayor,
        existencia,
        codigoProveedor,
        codigoTipoProducto
    FROM
        Productos
    WHERE
        codigoProducto = codigoProducto;
END $$
DELIMITER ;

call sp_buscarProducto(1);



DELIMITER $$
CREATE PROCEDURE sp_actualizarProducto (
    IN codigoProducto VARCHAR (45),
    IN descripcionProducto VARCHAR(45),
    IN precioUnitario DECIMAL(10,2),
    IN precioDocena DECIMAL(10,2),
    IN precioMayor DECIMAL(10,2),
    IN existencia INT,
    IN codigoProveedor INT,
    IN codigoTipoProducto INT
)
BEGIN
    UPDATE Productos SET
        Productos.descripcionProducto = descripcionProducto,
        Productos.precioUnitario = precioUnitario,
        Productos.precioDocena = precioDocena,
        Productos.precioMayor = precioMayor,
        Productos.existencia = existencia,
        Productos.codigoProveedor = codigoProveedor,
        Productos.codigoTipoProducto = codigoTipoProducto
    WHERE
        Productos.codigoProducto = codigoProducto;
END $$
DELIMITER ;
CALL sp_actualizarProducto('5', 'Camisa', 25.50, 28.00, 60.00, 100, 1, 1);



Delimiter $$
	Create procedure sp_eliminarProducto (in ProductoID varchar (45))
		Begin 
			Delete from Productos
				where codigoProducto = ProductoID;
		End $$
Delimiter ; 




------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_AgregarEmpleado (
    IN codigoEmpleado INT,
    IN nombresEmpleado VARCHAR(50),
    IN apellidosEmpleado VARCHAR(50),
    IN sueldo DECIMAL(10,2),
    IN direccion VARCHAR(150),
    IN turno VARCHAR(15),
    IN codigoCargoEmpleado INT
)
BEGIN
    INSERT INTO Empleados(codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado)
    VALUES (codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado);
END $$

DELIMITER ;

CALL sp_AgregarEmpleado(1, 'Juan', 'Pérez', 15.00, '123 Main Street', 'Matutino', 1);
CALL sp_AgregarEmpleado(2, 'María', 'González', 18.00, '456 Oak Avenue', 'Vespertino', 2);
CALL sp_AgregarEmpleado(3, 'Carlos', 'López', 16.00, '789 Elm Street', 'Nocturno', 3);
CALL sp_AgregarEmpleado(4, 'Laura', 'Martínez', 17.00, '321 Pine Street', 'Matutino', 4);
CALL sp_AgregarEmpleado(5, 'Ana', 'Sánchez', 19.00, '654 Cedar Avenue', 'Vespertino', 5);

DELIMITER $$

CREATE PROCEDURE sp_ListarEmpleado()
BEGIN
    SELECT * FROM Empleados;
END $$

DELIMITER ;

CALL sp_ListarEmpleado();


DELIMITER $$
CREATE PROCEDURE sp_buscarEmpleado(IN codigoEmpleado INT)
BEGIN
    SELECT
        codigoEmpleado,
        nombresEmpleado,
        apellidosEmpleado,
        sueldo,
        direccion,
        turno,
        codigoCargoEmpleado
    FROM
        Empleados
    WHERE
        codigoEmpleado = codigoEmpleado;
END $$
DELIMITER ;
 call sp_buscarEmpleado(1);


DELIMITER $$

CREATE PROCEDURE sp_actualizarEmpleado (
    IN codigoEmpleado INT,
    IN nombresEmpleado VARCHAR(50),
    IN apellidosEmpleado VARCHAR(50),
    IN sueldo DECIMAL(10,2),
    IN direccion VARCHAR(150),
    IN turno VARCHAR(15),
    IN codigoCargoEmpleado INT
)
BEGIN
    UPDATE Empleados SET
        Empleados.nombresEmpleado = nombresEmpleado,
        Empleados.apellidosEmpleado = apellidosEmpleado,
        Empleados.sueldo = sueldo,
        Empleados.direccion = direccion,
        Empleados.turno = turno,
        Empleados.codigoCargoEmpleado = codigoCargoEmpleado
    WHERE
        Empleados.codigoEmpleado = codigoEmpleado;
END $$

DELIMITER ;
CALL sp_actualizarEmpleado(1,'Juanito', 'Pérez',20.00,'123 Main Street','Nocturno',1);

DELIMITER $$

CREATE PROCEDURE sp_eliminarEmpleado ( CodigoEmpleadoID INT)
BEGIN
    DELETE FROM Empleados 
    WHERE codigoEmpleado = CodigoEmpleadoID;
END $$

DELIMITER ;

------------------------------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_AgregarEmailProveedor (
    IN codigoEmailProveedor INT,
    IN emailProveedor VARCHAR(50),
    IN descripcion VARCHAR(100),
    IN codigoProveedor INT
)
BEGIN
    INSERT INTO EmailProveedor(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
    VALUES (codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor);
END $$

DELIMITER ;

CALL sp_AgregarEmailProveedor(1, 'proveedor1@example.com', 'Email principal', 1);
CALL sp_AgregarEmailProveedor(2, 'proveedor2@example.com', 'Email secundario', 2);
CALL sp_AgregarEmailProveedor(3, 'proveedor3@example.com', 'Correo de contacto', 3);

DELIMITER $$

CREATE PROCEDURE sp_ListarEmailProveedor()
BEGIN
    SELECT * FROM EmailProveedor;
END $$

DELIMITER ;

CALL sp_ListarEmailProveedor();

DELIMITER $$

CREATE PROCEDURE sp_buscarEmailProveedor(IN codigoEmailProveedor INT)
BEGIN
    SELECT
        codigoEmailProveedor,
        emailProveedor,
        descripcion,
        codigoProveedor
    FROM
        EmailProveedor
    WHERE
        codigoEmailProveedor = codigoEmailProveedor;
END $$

DELIMITER ;

CALL sp_buscarEmailProveedor(1);

DELIMITER $$

CREATE PROCEDURE sp_ActualizarEmailProveedor (
    IN codigoEmailProveedor INT,
    IN emailProveedor VARCHAR(50),
    IN descripcion VARCHAR(100),
    IN codigoProveedor INT
)
BEGIN
    UPDATE EmailProveedor SET
        EmailProveedor.emailProveedor = emailProveedor,
        EmailProveedor.descripcion = descripcion,
        EmailProveedor.codigoProveedor = codigoProveedor
    WHERE
        EmailProveedor.codigoEmailProveedor = codigoEmailProveedor;
END $$

DELIMITER ;

CALL sp_ActualizarEmailProveedor(1, 'nuevo_email@example.com', 'Actualizado', 1);

DELIMITER $$

CREATE PROCEDURE sp_EliminarEmailProveedor (IN EmailProveedorID INT)
BEGIN
    DELETE FROM EmailProveedor
    WHERE codigoEmailProveedor = EmailProveedorID;
END $$

DELIMITER ;

-----------------------------------------------------------------
DELIMITER $$

CREATE PROCEDURE sp_AgregarTelefonoProveedor (
    IN codigoTelefonoProveedor INT,
    IN numeroPrincipal VARCHAR(8),
    IN numeroSecundario VARCHAR(8),
    IN observaciones VARCHAR(45),
    IN codigoProveedor INT
)
BEGIN
    INSERT INTO TelefonoProveedor(codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    VALUES (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor);
END $$

DELIMITER ;

CALL sp_AgregarTelefonoProveedor(1, '12345678', '87654321', 'Teléfono principal', 1);
CALL sp_AgregarTelefonoProveedor(2, '98765432', '23456789', 'Teléfono secundario', 2);
CALL sp_AgregarTelefonoProveedor(3, '11223344', '55667788', 'Teléfono de contacto', 3);

DELIMITER $$

CREATE PROCEDURE sp_ListarTelefonoProveedor()
BEGIN
    SELECT * FROM TelefonoProveedor;
END $$

DELIMITER ;

CALL sp_ListarTelefonoProveedor();

DELIMITER $$

CREATE PROCEDURE sp_buscarTelefonoProveedor(IN codigoTelefonoProveedor INT)
BEGIN
    SELECT
        codigoTelefonoProveedor,
        numeroPrincipal,
        numeroSecundario,
        observaciones,
        codigoProveedor
    FROM
        TelefonoProveedor
    WHERE
        codigoTelefonoProveedor = codigoTelefonoProveedor;
END $$

DELIMITER ;

CALL sp_buscarTelefonoProveedor(1);

DELIMITER $$

CREATE PROCEDURE sp_ActualizarTelefonoProveedor (
    IN codigoTelefonoProveedor INT,
    IN numeroPrincipal VARCHAR(8),
    IN numeroSecundario VARCHAR(8),
    IN observaciones VARCHAR(45),
    IN codigoProveedor INT
)
BEGIN
    UPDATE TelefonoProveedor SET
        TelefonoProveedor.numeroPrincipal = numeroPrincipal,
        TelefonoProveedor.numeroSecundario = numeroSecundario,
        TelefonoProveedor.observaciones = observaciones,
        TelefonoProveedor.codigoProveedor = codigoProveedor
    WHERE
        TelefonoProveedor.codigoTelefonoProveedor = codigoTelefonoProveedor;
END $$

DELIMITER ;

CALL sp_ActualizarTelefonoProveedor(1, '11112222', '33334444', 'Actualizado', 1);

DELIMITER $$

CREATE PROCEDURE sp_EliminarTelefonoProveedor (IN CodigoTelefonoProveedorID INT)
BEGIN
    DELETE FROM TelefonoProveedor
    WHERE codigoTelefonoProveedor = CodigoTelefonoProveedorID;
END $$

DELIMITER ;
-------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarDetalleCompra (
    IN codigoDetalleCompra INT,
    IN costoUnitario DECIMAL(10,2),
    IN cantidad INT,
    IN codigoProducto VARCHAR(45),
    IN numeroDocumento INT
)
BEGIN
    INSERT INTO DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    VALUES (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento);
END $$

DELIMITER ;
CALL sp_AgregarDetalleCompra(1, 20.00, 2, '1', 1);
CALL sp_AgregarDetalleCompra(2, 30.00, 1, '2', 2);
CALL sp_AgregarDetalleCompra(3, 40.00, 3, '3', 3);
CALL sp_AgregarDetalleCompra(4, 15.00, 2, '4', 4);

DELIMITER $$
CREATE PROCEDURE sp_ListarDetallesCompra()
BEGIN
    SELECT * FROM DetalleCompra;
END $$

DELIMITER ;

CALL sp_ListarDetallesCompra();

DELIMITER $$

CREATE PROCEDURE sp_buscarDetalleCompra(IN codigoDetalleCompra INT)
BEGIN
    SELECT
        codigoDetalleCompra,
        costoUnitario,
        cantidad,
        codigoProducto,
        numeroDocumento
    FROM
        DetalleCompra
    WHERE
        codigoDetalleCompra = codigoDetalleCompra;
END $$

DELIMITER ;

CALL sp_buscarDetalleCompra(1);


DELIMITER $$
CREATE PROCEDURE sp_ActualizarDetalleCompra (
    IN codigoDetalleCompra INT,
    IN costoUnitario DECIMAL(10,2),
    IN cantidad INT,
    IN codigoProducto VARCHAR(45),
    IN numeroDocumento INT
)
BEGIN
    UPDATE DetalleCompra SET 
		DetalleCompra.costoUnitario = costoUnitario,
        DetalleCompra.cantidad = cantidad,
        DetalleCompra.codigoProducto = codigoProducto,
        DetalleCompra.numeroDocumento = numeroDocumento
    WHERE DetalleCompra.codigoDetalleCompra = codigoDetalleCompra;
END $$
DELIMITER ;

CALL sp_ActualizarDetalleCompra(4, 95.00, 2, '4', 4);

DELIMITER $$
CREATE PROCEDURE sp_EliminarDetalleCompra (IN p_codigoDetalleCompra INT)
BEGIN
    DELETE FROM DetalleCompra 
    WHERE codigoDetalleCompra = p_codigoDetalleCompra;
END $$

DELIMITER ;

---------------------------------------------------------------------
DELIMITER $$

CREATE PROCEDURE sp_AgregarFactura (
    IN numeroFactura INT,
    IN estado VARCHAR(50),
    IN totalFactura DECIMAL(10,2),
    IN fechaFactura VARCHAR(45),
    IN clienteId INT,
    IN codigoEmpleado INT
)
BEGIN
    INSERT INTO Factura (numeroFactura, estado, totalFactura, fechaFactura, clienteId, codigoEmpleado)
    VALUES (numeroFactura, estado, totalFactura, fechaFactura, clienteId, codigoEmpleado);
END $$

DELIMITER ;

CALL sp_AgregarFactura(1, 'Pagada', 150.50, '2024-05-19', 1, 1);
CALL sp_AgregarFactura(2, 'Pendiente', 200.75, '2024-05-18', 2, 2);
CALL sp_AgregarFactura(3, 'Cancelada', 300.00, '2024-05-17', 3, 3);
CALL sp_AgregarFactura(4, 'Pagada', 450.25, '2024-05-16', 4, 4);



DELIMITER $$

CREATE PROCEDURE sp_ListarFactura()
BEGIN
    SELECT * FROM Factura;
END $$

DELIMITER ;

CALL sp_ListarFactura();


DELIMITER $$

CREATE PROCEDURE sp_BuscarFactura (IN numeroFactura INT)
BEGIN
    SELECT
        numeroFactura,
        estado,
        totalFactura,
        fechaFactura,
        clienteId,
        codigoEmpleado
    FROM
        Factura
    WHERE
        numeroFactura = numeroFactura;
END $$

DELIMITER ;


CALL sp_BuscarFactura(1);


DELIMITER $$

CREATE PROCEDURE sp_ActualizarFactura (
    IN numeroFactura INT,
    IN estado VARCHAR(50),
    IN totalFactura DECIMAL(10,2),
    IN fechaFactura VARCHAR(45),
    IN clienteId INT,
    IN codigoEmpleado INT
)
BEGIN
    UPDATE Factura SET 
		Factura.estado = estado,
        Factura.totalFactura = totalFactura,
        Factura.fechaFactura = fechaFactura,
        Factura.clienteId = clienteId,
        Factura.codigoEmpleado = codigoEmpleado
    WHERE Factura.numeroFactura = numeroFactura;
END $$

DELIMITER ;

CALL sp_ActualizarFactura(1, 'Pendiente', 155.75, '2024-05-19', 1, 1);


DELIMITER $$

CREATE PROCEDURE sp_EliminarFactura (
    IN p_numeroFactura INT
)
BEGIN
    DELETE FROM Factura WHERE numeroFactura = p_numeroFactura;
END $$

DELIMITER ;
----------------------------------------------------
DELIMITER $$

CREATE PROCEDURE sp_AgregarDetalleFactura (
    IN codigoDetalleFactura INT,
    IN precioUnitario DECIMAL(10,2),
    IN cantidad INT,
    IN numeroFactura INT,
    IN codigoProducto VARCHAR(45)
)
BEGIN
    INSERT INTO DetalleFactura (codigoDetalleFactura, precioUnitario, cantidad, numeroFactura, codigoProducto)
    VALUES (codigoDetalleFactura, precioUnitario, cantidad, numeroFactura, codigoProducto);
END $$

DELIMITER ;

CALL sp_AgregarDetalleFactura(1, 25.50, 2, 1, '1');
CALL sp_AgregarDetalleFactura(2, 35.75, 1, 2, '2');
CALL sp_AgregarDetalleFactura(3, 50.00, 3, 3, '3');
CALL sp_AgregarDetalleFactura(4, 15.25, 4, 4, '4');

DELIMITER $$

CREATE PROCEDURE sp_ListarDetallesFactura()
BEGIN
    SELECT * FROM DetalleFactura;
END $$

DELIMITER ;
CALL sp_ListarDetallesFactura();

DELIMITER $$

CREATE PROCEDURE sp_BuscarDetalleFactura (
    IN p_codigoDetalleFactura INT
)
BEGIN
    SELECT
        codigoDetalleFactura,
        precioUnitario,
        cantidad,
        numeroFactura,
        codigoProducto
    FROM
        DetalleFactura
    WHERE
        codigoDetalleFactura = p_codigoDetalleFactura;
END $$

DELIMITER ;

CALL sp_BuscarDetalleFactura(1);

DELIMITER $$

CREATE PROCEDURE sp_ActualizarDetalleFactura (
    IN codigoDetalleFactura INT,
    IN precioUnitario DECIMAL(10,2),
    IN cantidad INT,
    IN numeroFactura INT,
    IN codigoProducto VARCHAR(45)
)
BEGIN
    UPDATE DetalleFactura SET
		DetalleFactura.precioUnitario = precioUnitario,
        DetalleFactura.cantidad = cantidad,
        DetalleFactura.numeroFactura = numeroFactura,
        DetalleFactura.codigoProducto = codigoProducto
    WHERE DetalleFactura.codigoDetalleFactura = codigoDetalleFactura;
END $$

DELIMITER ;

CALL sp_ActualizarDetalleFactura(1, 30.00, 2, 1, '1');

DELIMITER $$

CREATE PROCEDURE sp_EliminarDetalleFactura (
    IN p_codigoDetalleFactura INT
)
BEGIN
    DELETE FROM DetalleFactura WHERE codigoDetalleFactura = p_codigoDetalleFactura;
END $$
DELIMITER ;

select * from DetalleFactura
	join Factura on DetalleFactura.numeroFactura = Factura.numeroFactura
    join Clientes on Factura.clienteId = Clientes.clienteId
    join Productos on DetalleFactura.codigoProducto = Productos.codigoProducto
	where Factura.numeroFactura = 2;