package org.dannyrodriguez.bean;



public class Proveedor {

    private int codigoProveedor;
    private String NITProveedor;
    private String nombreProveedor;
    private String apellidoProveedor;
    private String direccionProveedor;
    private String razonSocial;
    private String contactoPrincipal;
    private String paginaWeb;

    public Proveedor() {
    }

    public Proveedor(int codigoProveedor, String NITProveedor, String nombreProveedor, String apellidoProveedor, String direccionProveedor, String razonSocial, String contactoPrincipal, String paginaWeb) {
        this.codigoProveedor = codigoProveedor;
        this.NITProveedor = NITProveedor;
        this.nombreProveedor = nombreProveedor;
        this.apellidoProveedor = apellidoProveedor;
        this.direccionProveedor = direccionProveedor;
        this.razonSocial = razonSocial;
        this.contactoPrincipal = contactoPrincipal;
        this.paginaWeb = paginaWeb;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getNITProveedor() {
        return NITProveedor;
    }

    public void setNITProveedor(String NITProveedor) {
        this.NITProveedor = NITProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getApellidoProveedor() {
        return apellidoProveedor;
    }

    public void setApellidoProveedor(String apellidoProveedor) {
        this.apellidoProveedor = apellidoProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }
    
    @Override
    public String toString() {
        return getNITProveedor()+ " | " + getNombreProveedor();
    }

   
}
