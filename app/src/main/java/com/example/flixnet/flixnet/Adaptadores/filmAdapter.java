package com.example.flixnet.flixnet.Adaptadores;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.flixnet.flixnet.Modelos.Pelicula;
import com.example.flixnet.flixnet.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Programación multimedia y dispositivos móviles
 * @author Antonio J.Sánchez
 * @year 2018/19
 *
 */
public class filmAdapter extends RecyclerView.Adapter<filmAdapter.filmHolder> {

    // Nuestro adaptador necesitará básicamente tres elementos
    // - layout
    //   define la manera en que se dibujará en pantalla cada ítem (película)
    // - data
    //   colección de ítems (películas) que manejará el adaptador
    // - listener
    //   permitirá asociar un "escuchador" a cada ítem para que se lance un
    //   determinado evento cuando el usuario haga click sobre un ítem.
    private int layout ;
    private List<Pelicula> data ;
    private OnItemClickListener listener ;

    //
    // Guardaremos además el valor de contexto que se nos pasa, para poder
    // trabajar posteriormente con la librería PICASSO (se ha añadido la
    // referencia build.gradle)
    private Context contexto ;

    /**
     * @param contexto
     * @param layout
     * @param data
     * @param listener
     */
    public filmAdapter(Context contexto, int layout, List<Pelicula> data, OnItemClickListener listener) {
        this.listener = listener ;
        this.contexto = contexto ;
        this.layout   = layout;
        this.data     = data;
    }

    /**
     * @param viewGroup
     * conjunto de vistas (RecyclerView) donde se añadirán las del layout que se nos pasó
     * en el constructor.
     * @param viewType
     * define el tipo de la nueva vista
     * @return
     * un objeto de tipo filmHolder
     */
    @NonNull
    @Override
    public filmHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        // Cada holder contendrá información sobre los ítems que está gestionando el
        // adaptador, almacenando las diferentes vistas del layout que se nos pasó
        // en el constructor de la clase filmAdapter. Para ello, necesitamos añadir
        // al recyclerView estas vistas, siendo necesario realizar un proceso de
        // "inflado".

        // Obtenemos en primer lugar el inflador, que necesitará conocer el contexto
        // en el que vamos a inyectar los elementos de nuestro layout.
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext()) ;

        // Obtenido el inflador, inflamos el layout, esto es, añadimos las vistas de
        // éste a las que ya teníamos en viewGroup, generando una nueva vista.
        View vista = inflater.inflate(this.layout, null) ;

        // Creamos nuestro holder y le proporcionamos la vista obtenida anteriormente.
        filmHolder holder = new filmHolder(vista) ;

        // Devolvemos el holder.
        return holder ;
    }

    /**
     * @param filmHolder
     * recibe un determinado holder
     * @param pos
     * identifica un determinado ítem dentro del conjunto de datos que maneja el
     * adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull filmHolder filmHolder, int pos) {

        // Asociamos al holder el ítem (película) y el listener que deberá estar
        // atento a si el usuario selecciona ese elemento.
        filmHolder.bindItem(this.data.get(pos), this.listener) ;
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        // Devolvemos el número de elementos que está gestionando el adaptador,
        // y que se encuentran almacenados en la propiedad DATA.
        return this.data.size() ;
    }

    /**
     * Definimos la clase filmHolder que, como ya dijimos en clase, nos permitirá
     * obtener un rendimiento mayor en la gestión y render de listas de datos. Sea
     * como fuere, usando un control RecyclerView nos vemos obligados a implementar
     * este patrón de diseño.
     *
     * Gracias a la clase filmHolder, guardaremos las vistas asociadas a cada ítem
     * de nuestro conjunto de datos, no siendo necesaria buscarlas cada vez que
     * queramos dibujar el elemento en pantalla.
     */
    public class filmHolder extends RecyclerView.ViewHolder {

        private ImageView poster ;
        private TextView  titulo ;
        private RatingBar rating ;

        /**
         * @param itemView
         * conjunto de vistas entre las que encontraremos las de nuestro layout;
         * en este caso, poster, titulo y rating.
         */
        public filmHolder(@NonNull View itemView) {
            super(itemView);

            // Instanciamos y guardamos una referencia a cada elemento del
            // layout: list_film_item.xml
            poster = itemView.findViewById(R.id.filmPoster) ;
            titulo = itemView.findViewById(R.id.filmTitle) ;
            rating = itemView.findViewById(R.id.filmRating) ;
        }

        /**
         * @param item
         * elemento que estamos asociando/guardando en el holder
         * @param listener
         * referencia al "escuchador" que responderá a una pulsación del usuario sobre
         * este ítem en particular.
         */
        public void bindItem(final Pelicula item, final OnItemClickListener listener) {

            // Mostramos el título de la película
            titulo.setText(item.getTitulo()) ;

            // Establecemos el rating/nota
            rating.setRating(item.getNota());

            // Mostramos la imagen del poster, para lo cual utilizaremos la librería PICASSO,
            // que simplificará (muy mucho) nuestro trabajo. Previamente, debemos investigar
            // en la documentación de la API de TMDB, la URL base de acceso a las imágenes y
            // descubrimos que es: http://image.tmdb.org/t/p/w185/

            // Necesitamos proporcionarle a PICASSO el contexto en el que vamos a trabajar.
            // Hecho esto, le indicamos que cargue la imagen en la vista POSTER.
            Picasso.with(contexto)
                .load("http://image.tmdb.org/t/p/w185" + item.getPoster())
                .into(poster) ;

            // Asociamos un "escuchador" al ítem y, desde el evento onClick de éste, llamamos
            // mos al evento onItemClick que hemos definido en nuestra interfaz. Nos vemos
            // obligados a hacer eso ya que, desde la actividad principal nos sería imposible
            // conocer el elemento sobre el que el usuario ha hecho click. Desde el propio
            // adaptador, podemos conocer dicha posición utilizando getAdapterPosition().
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, getAdapterPosition());
                }
            });
        }
    }

    /**
     * Implementamos una interface que nos permitirá definir el evento que responderá
     * a una pulsación del usuario sobre uno de los ítems del RecyclerView. El evento
     * recibirá el ítem sobre el que hemos pulsado y la posición de éste en el adapta-
     * dor.
     */
    public interface OnItemClickListener {
        void onItemClick(Pelicula film, int position) ;
    }

}
