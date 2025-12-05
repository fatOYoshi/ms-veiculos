const API_BASE_URL = "/api/veiculos";
const veiculosTbody = document.getElementById("veiculos-tbody");
const veiculoForm = document.getElementById("veiculo-form");
let veiculoEmEdicao = null;

// 1. Função para carregar e exibir a lista de veículos
async function carregarVeiculos() {
  veiculosTbody.innerHTML = ""; // Limpa a tabela
  try {
    const response = await fetch(API_BASE_URL);

    if (!response.ok) {
      throw new Error(`Erro ao buscar veículos: ${response.status}`);
    }

    const veiculos = await response.json();

    if (veiculos.length === 0) {
      veiculosTbody.innerHTML =
        '<tr><td colspan="7" style="text-align: center; padding: 20px;">Nenhum veículo cadastrado.</td></tr>';
      return;
    }

    veiculos.forEach((veiculo) => {
      const row = veiculosTbody.insertRow();

      // ID
      row.insertCell(0).textContent = veiculo.id || "";

      // Placa
      row.insertCell(1).textContent = veiculo.placa || "";

      // Marca
      row.insertCell(2).textContent = veiculo.marca || "";

      // Modelo
      row.insertCell(3).textContent = veiculo.modelo || "";

      // Ano
      row.insertCell(4).textContent = veiculo.ano || "";

      // Status
      const statusCell = row.insertCell(5);
      statusCell.innerHTML = formatarStatus(veiculo.status || "");

      // Ações
      const acoesCell = row.insertCell(6);
      acoesCell.style.textAlign = "center";

      // Botão Editar
      const editarBtn = document.createElement("button");
      editarBtn.textContent = "Editar";
      editarBtn.style.cssText =
        "background-color: #28a745; color: white; border: none; padding: 5px 10px; margin: 2px; border-radius: 3px; cursor: pointer;";
      editarBtn.onclick = () => prepararEdicao(veiculo);

      // Botão Excluir
      const excluirBtn = document.createElement("button");
      excluirBtn.textContent = "Excluir";
      excluirBtn.style.cssText =
        "background-color: #dc3545; color: white; border: none; padding: 5px 10px; margin: 2px; border-radius: 3px; cursor: pointer;";
      excluirBtn.onclick = () => excluirVeiculo(veiculo.id);

      acoesCell.appendChild(editarBtn);
      acoesCell.appendChild(excluirBtn);
    });
  } catch (error) {
    console.error("Erro ao carregar veículos:", error);
    veiculosTbody.innerHTML =
      '<tr><td colspan="7" style="text-align: center; color: red; padding: 20px;">Erro ao carregar veículos. Verifique se o backend está rodando.</td></tr>';
  }
}

// Função para formatar o status
function formatarStatus(status) {
  if (!status || status.trim() === "") {
    return '<span style="color: gray;">N/A</span>';
  }

  const statusLowerCase = status.toLowerCase();
  let cor = "#6c757d"; // cinza padrão

  if (statusLowerCase === "disponivel") {
    cor = "#28a745"; // verde
  } else if (statusLowerCase === "alugado") {
    cor = "#ffc107"; // amarelo
  } else if (statusLowerCase === "manutencao") {
    cor = "#dc3545"; // vermelho
  }

  return `<span style="background-color: ${cor}; color: white; padding: 3px 8px; border-radius: 10px; font-size: 12px;">
    ${status.charAt(0).toUpperCase() + status.slice(1)}
  </span>`;
}

// 2. Função para cadastrar ou atualizar veículo
veiculoForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  // Coletando dados dos campos
  const placa = document.getElementById("placa").value.trim();
  const marca = document.getElementById("marca").value.trim();
  const modelo = document.getElementById("modelo").value.trim();
  const ano = document.getElementById("ano").value.trim();
  const status = document.getElementById("status").value;

  // Validação básica
  if (!placa || !marca || !modelo || !ano || !status) {
    alert("Por favor, preencha todos os campos!");
    return;
  }

  const veiculoData = {
    placa: placa,
    marca: marca,
    modelo: modelo,
    ano: parseInt(ano),
    status: status,
  };

  try {
    const btnSubmit = document.querySelector(
      "#veiculo-form button[type='submit']"
    );

    if (veiculoEmEdicao) {
      // MODE: ATUALIZAR
      const response = await fetch(`${API_BASE_URL}/${veiculoEmEdicao.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(veiculoData),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Falha ao atualizar veículo");
      }

      alert("Veículo atualizado com sucesso!");
      btnSubmit.textContent = "Cadastrar Veículo";
      btnSubmit.style.backgroundColor = "#007bff";
      veiculoEmEdicao = null;
    } else {
      // MODE: CADASTRAR
      const response = await fetch(API_BASE_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(veiculoData),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Falha ao cadastrar veículo");
      }

      alert("Veículo cadastrado com sucesso!");
    }

    veiculoForm.reset(); // Limpa o formulário
    carregarVeiculos(); // Recarrega a lista
  } catch (error) {
    console.error("Erro:", error);
    alert(`Erro: ${error.message}`);
  }
});

// 3. Função para excluir veículo
async function excluirVeiculo(id) {
  if (!confirm("Tem certeza que deseja excluir este veículo?")) return;

  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    console.log("Resposta DELETE:", response.status); // Para depuração

    if (response.ok) {
      // Se status for 200 OK
      const result = await response.text();
      console.log("Resultado:", result);
      alert("✅ Veículo excluído com sucesso!");
      carregarVeiculos();
    } else if (response.status === 404) {
      const error = await response.text();
      throw new Error("Veículo não encontrado");
    } else {
      const error = await response.text();
      throw new Error(`Erro ${response.status}: Não foi possível excluir`);
    }
  } catch (error) {
    console.error("Erro ao excluir:", error);
    alert(`❌ ${error.message}`);
  }
}

// 4. Função para preparar edição
function prepararEdicao(veiculo) {
  veiculoEmEdicao = veiculo;

  // Preenche o formulário com os dados do veículo
  document.getElementById("placa").value = veiculo.placa || "";
  document.getElementById("marca").value = veiculo.marca || "";
  document.getElementById("modelo").value = veiculo.modelo || "";
  document.getElementById("ano").value = veiculo.ano || "";
  document.getElementById("status").value = veiculo.status || "";

  // Muda o botão para "Atualizar"
  const btnSubmit = document.querySelector(
    "#veiculo-form button[type='submit']"
  );
  btnSubmit.textContent = "Atualizar Veículo";
  btnSubmit.style.backgroundColor = "#28a745";

  // Rolagem para o formulário
  document
    .getElementById("cadastro-form")
    .scrollIntoView({ behavior: "smooth" });
}

// 5. Cancelar edição
function cancelarEdicao() {
  veiculoEmEdicao = null;
  veiculoForm.reset();
  const btnSubmit = document.querySelector(
    "#veiculo-form button[type='submit']"
  );
  btnSubmit.textContent = "Cadastrar Veículo";
  btnSubmit.style.backgroundColor = "#007bff";
}

// 6. Carrega os veículos quando a página carrega
document.addEventListener("DOMContentLoaded", function () {
  carregarVeiculos();

  // Adiciona botão de cancelar se não existir
  if (!document.getElementById("cancelar-edicao")) {
    const btnCancelar = document.createElement("button");
    btnCancelar.id = "cancelar-edicao";
    btnCancelar.textContent = "Cancelar Edição";
    btnCancelar.type = "button";
    btnCancelar.style.cssText =
      "background-color: #6c757d; color: white; border: none; padding: 12px; margin-left: 10px; border-radius: 4px; cursor: pointer;";
    btnCancelar.onclick = cancelarEdicao;
    veiculoForm.appendChild(btnCancelar);
  }
});
