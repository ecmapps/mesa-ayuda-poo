package diccionario;

public class RegistroEmocion {

    private String nombre;
    private String emocion;
    private String intensidad;
    private String duracion;
    private String comentario;

    public RegistroEmocion() {
    }

    public RegistroEmocion(String nombre,
                           String emocion,
                           String intensidad,
                           String duracion,
                           String comentario)
    {
        this.nombre = nombre;
        this.emocion = emocion;
        this.intensidad = intensidad;
        this.duracion = duracion;
        this.comentario = comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmocion() {
        return emocion;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getComentario() {
        return comentario;
    }
}