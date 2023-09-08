package me.dio.sacola.resource;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value="/ifood-devweek/sacolas")
@RestController
@RequestMapping("ifood-devweek/sacolas")
@RequiredArgsConstructor
public class SacolaResource {
    private final SacolaService sacolaService;

    @PostMapping
    public Item incluirItemNaSacole(@RequestBody ItemDto itemDto) {
        return sacolaService.incluirItemNaSacola(itemDto);
    }

    @GetMapping("/{id}")
    public Sacola verSacola(@PathVariable("id") Long id) {
        return sacolaService.verSacola(id);
    }

    @PatchMapping("/fecharSacola/{sacolaId}")
    public Sacola fecharSacola(@PathVariable("sacolaId") Long sacolaId, @RequestParam("formaPagamento") int formaPagamento) {
        return sacolaService.fecharSacola(sacolaId, formaPagamento);
    }

    @DeleteMapping("/{sacolaId}/itens/{itemId}") // Adicione um endpoint para excluir um item
    public ResponseEntity<String> excluirItemDaSacola(@PathVariable("sacolaId") Long sacolaId, @PathVariable("itemId") Long itemId) {
        try {
            sacolaService.excluirItemDaSacola(sacolaId, itemId);
            return ResponseEntity.ok("Item removido com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
