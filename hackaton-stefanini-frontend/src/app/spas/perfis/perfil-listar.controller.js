angular.module("hackaton-stefanini").controller("PerfilsListarController", PerfilsListarController);
PerfilsListarController.$inject = ["$rootScope", "$scope", "$location",
  "$q", '$filter', '$routeParams', 'HackatonStefaniniService'];



function PerfilsListarController($rootScope, $scope, $location,
  $q, $filter, $routeParams, HackatonStefaniniService) {
  vm = this;


  vm.qdePorPagina = 5;
  vm.ultimoIndex = 0;
  vm.contador = 0;

  vm.urlPerfil = "http://localhost:8080/treinamento/api/perfils/";

  vm.init = function () {

    HackatonStefaniniService.listar(vm.urlPerfil).then(
      function (responsePerfils) {
        if (responsePerfils.data !== undefined)

          vm.listaPerfis = responsePerfils.data;

      })
  };

  vm.retornarTelaListagem = function () {
    $location.path("listarPessoas");
  }

  vm.editar = function (id) {
    if (id !== undefined)
      $location.path("EditarPerfis/" + id);

    else
      $location.path("EditarPerfis");
  }
  vm.remover = function (id) {
    HackatonStefaniniService.excluir(vm.urlPerfil + id).then(

      function (response) {
        vm.init();
      });
  }

  vm.formataDataTela = function (data) {
    var ano = data.slice(0, 4);
    var mes = data.slice(5, 7);
    var dia = data.slice(8, 10);

    return dia + "/" + mes + "/" + ano;
  };


}