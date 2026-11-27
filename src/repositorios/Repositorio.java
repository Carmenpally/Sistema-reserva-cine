/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorios;

import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

/**
 *
 * @author max_p
 * @param <T> Tipo de elemento que implementa Identificable.
 */
public class Repositorio<T extends Identificable> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Map<String, T> elementos;
    
    public Repositorio(){
        
        this.elementos = new HashMap<> ();
        
    }
    
    public void agregar(T elemento) throws ReglaNegocioException{
        if (elemento == null){
            throw new ReglaNegocioException(
            "El elemento no puede ser nulo."
            );
        }
        
        String codigo = normalizarCodigo(elemento.getCodigo());
        
        if (elementos.containsKey(codigo)){
            throw new ReglaNegocioException(
            "Ya existe un elemento con ese código."
            );
        }
        
        elementos.put(codigo,elemento);
    }
    public T buscar(String codigo) {
        if (codigo == null) {
        return null;
        }

    return elementos.get(normalizarCodigo(codigo));
}
    public boolean existe(String codigo) {
        if (codigo == null) {
        return false;
        }

    return elementos.containsKey(normalizarCodigo(codigo));
}
    
    public List<T> listar(){
        return new ArrayList<>(elementos.values());
    }
    
    public int cantidad(){
        return elementos.size();
    }
    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
    
}
