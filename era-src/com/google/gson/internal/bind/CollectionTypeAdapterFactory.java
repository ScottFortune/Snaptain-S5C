package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

public final class CollectionTypeAdapterFactory implements TypeAdapterFactory {
  private final ConstructorConstructor constructorConstructor;
  
  public CollectionTypeAdapterFactory(ConstructorConstructor paramConstructorConstructor) {
    this.constructorConstructor = paramConstructorConstructor;
  }
  
  public <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken) {
    Type type = paramTypeToken.getType();
    Class<?> clazz = paramTypeToken.getRawType();
    if (!Collection.class.isAssignableFrom(clazz))
      return null; 
    type = .Gson.Types.getCollectionElementType(type, clazz);
    return new Adapter(paramGson, type, paramGson.getAdapter(TypeToken.get(type)), this.constructorConstructor.get(paramTypeToken));
  }
  
  private static final class Adapter<E> extends TypeAdapter<Collection<E>> {
    private final ObjectConstructor<? extends Collection<E>> constructor;
    
    private final TypeAdapter<E> elementTypeAdapter;
    
    public Adapter(Gson param1Gson, Type param1Type, TypeAdapter<E> param1TypeAdapter, ObjectConstructor<? extends Collection<E>> param1ObjectConstructor) {
      this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(param1Gson, param1TypeAdapter, param1Type);
      this.constructor = param1ObjectConstructor;
    }
    
    public Collection<E> read(JsonReader param1JsonReader) throws IOException {
      if (param1JsonReader.peek() == JsonToken.NULL) {
        param1JsonReader.nextNull();
        return null;
      } 
      Collection<Object> collection = (Collection)this.constructor.construct();
      param1JsonReader.beginArray();
      while (param1JsonReader.hasNext())
        collection.add(this.elementTypeAdapter.read(param1JsonReader)); 
      param1JsonReader.endArray();
      return (Collection)collection;
    }
    
    public void write(JsonWriter param1JsonWriter, Collection<E> param1Collection) throws IOException {
      if (param1Collection == null) {
        param1JsonWriter.nullValue();
        return;
      } 
      param1JsonWriter.beginArray();
      for (Collection<E> param1Collection : param1Collection)
        this.elementTypeAdapter.write(param1JsonWriter, param1Collection); 
      param1JsonWriter.endArray();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/internal/bind/CollectionTypeAdapterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */