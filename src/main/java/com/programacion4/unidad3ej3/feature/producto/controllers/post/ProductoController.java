package com.programacion4.unidad3ej3.feature.producto.controllers.post;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programacion4.unidad3ej3.config.BaseResponse;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.response.ProductoResponseDto;
import com.programacion4.unidad3ej3.feature.producto.services.impl.domain.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ProductoResponseDto>> crearProducto(
            @Valid @RequestBody ProductoCreateRequestDto dto) {

        ProductoResponseDto productoCreado = productoService.crearProducto(dto);

        BaseResponse<ProductoResponseDto> response =
                BaseResponse.ok(productoCreado, "Producto creado correctamente");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductoResponseDto>>> obtenerTodos() {

        List<ProductoResponseDto> productos = productoService.obtenerTodos();

        BaseResponse<List<ProductoResponseDto>> response =
                BaseResponse.ok(productos, "Listado de productos obtenido correctamente");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductoResponseDto>> obtenerPorId(@PathVariable Long id) {

    ProductoResponseDto producto = productoService.obtenerPorId(id);

    BaseResponse<ProductoResponseDto> response =
            BaseResponse.ok(producto, "Producto obtenido correctamente");

    return ResponseEntity.ok(response);
    }
}