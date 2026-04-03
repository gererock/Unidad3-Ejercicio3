package com.programacion4.unidad3ej3.feature.producto.services.impl.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.programacion4.unidad3ej3.config.exceptions.ConflictException;
import com.programacion4.unidad3ej3.config.exceptions.ResourceNotFoundException;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoPatchRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoUpdateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.response.ProductoResponseDto;
import com.programacion4.unidad3ej3.feature.producto.mappers.ProductoMapper;
import com.programacion4.unidad3ej3.feature.producto.models.Producto;
import com.programacion4.unidad3ej3.feature.producto.repositories.IProductoRepository;

@Service
public class ProductoService {

    private final IProductoRepository productoRepository;

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoResponseDto crearProducto(ProductoCreateRequestDto dto) {

        String nombreFormateado = capitalizar(dto.getNombre());
        String descripcionFormateada = capitalizar(dto.getDescripcion());

        if (productoRepository.existsByNombre(nombreFormateado)) {
            throw new ConflictException("Ya existe un producto con ese nombre");
        }

        Producto producto = ProductoMapper.toEntity(dto);

        producto.setNombre(nombreFormateado);
        producto.setDescripcion(descripcionFormateada);
        producto.setEstaEliminado(false);

        Producto productoGuardado = productoRepository.save(producto);

        return ProductoMapper.toResponseDto(productoGuardado);
    }

    public List<ProductoResponseDto> obtenerTodos() {
        List<ProductoResponseDto> productosResponse = new ArrayList<>();

        Iterable<Producto> productos = productoRepository.findAll();

        for (Producto producto : productos) {
            productosResponse.add(ProductoMapper.toResponseDto(producto));
        }

        return productosResponse;
    }

    public ProductoResponseDto obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id: " + id));

        return ProductoMapper.toResponseDto(producto);
    
    }


    public ProductoResponseDto actualizarTotal(Long id, ProductoUpdateRequestDto dto) {
    Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id: " + id));

    producto.setNombre(capitalizar(dto.getNombre()));
    producto.setCodigo(dto.getCodigo());
    producto.setDescripcion(capitalizar(dto.getDescripcion()));
    producto.setPrecio(dto.getPrecio());
    producto.setStock(dto.getStock());

    Producto productoActualizado = productoRepository.save(producto);

    return ProductoMapper.toResponseDto(productoActualizado);

    }



    public ProductoResponseDto actualizarParcial(Long id, ProductoPatchRequestDto dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id: " + id));

        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }

        if (dto.getStock() != null) {
            producto.setStock(dto.getStock());
        }

        Producto productoActualizado = productoRepository.save(producto);

        return ProductoMapper.toResponseDto(productoActualizado);
    }

    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id: " + id));

        producto.setEstaEliminado(true);
        productoRepository.save(producto);
    }

    private String capitalizar(String texto) {
        texto = texto.trim().toLowerCase();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}