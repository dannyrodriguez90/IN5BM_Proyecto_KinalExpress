package org.dannyrodriguez.bean;

import java.math.BigDecimal;
import java.sql.Date;

public class Compras {

    private int numeroDocumento;
    private Date fechaDocumento;
    private String descripcion;
    private BigDecimal totalDocumento;

    public Compras() {

    }

    public Compras(int numeroDocumento, Date fechaDocumento, String descripcion, BigDecimal totalDocumento) {
        this.numeroDocumento = numeroDocumento;
        this.fechaDocumento = fechaDocumento;
        this.descripcion = descripcion;
        this.totalDocumento = totalDocumento;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getTotalDocumento() {
        return totalDocumento;
    }

    public void setTotalDocumento(BigDecimal totalDocumento) {
        this.totalDocumento = totalDocumento;
    }
}
