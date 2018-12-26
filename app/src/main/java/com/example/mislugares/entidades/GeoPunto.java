package com.example.mislugares.entidades;

public class GeoPunto {
    private double longitud, latitud;

    public GeoPunto() {}

    public GeoPunto(double longitud, double latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String toString() {
        return "longitud:" + longitud + ", latitud:" + latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double distancia(GeoPunto punto) {
        final double RADIO_TIERRA = 6371000; // en metros
        double dLat = Math.toRadians(latitud - punto.latitud);
        double dLon = Math.toRadians(longitud - punto.longitud);
        double lat1 = Math.toRadians(punto.latitud);
        double lat2 = Math.toRadians(latitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                        Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * RADIO_TIERRA;
    }

    /*
    public String getTipo(){
        if (tipo == null) return null;
        else return tipo.name();
        }
    public void setTipo(String nombre){
        if (nombre == null) tipo = null;
        else tipo = TipoLugar.valueOf(nombre);
    }
    * */
}
