angular.module("hackaton-stefanini").controller("PerfilListarController", PerfilListarController);
PerfilListarController.$inject = ["$rootScope", "$scope", "$location",
  "$q", '$filter', '$routeParams', 'HackatonStefaniniService'];



function PerfilListarController($rootScope, $scope, $location,
  $q, $filter, $routeParams, HackatonStefaniniService) {
  vm = this;


  vm.urlPerfil = "http://localhost:8080/treinamento/api/perfis/";

  vm.init = function () {

    HackatonStefaniniService.listar(vm.urlPerfil).then(
      function (responsePerfis) {
        if (responsePerfis.data !== undefined)

          vm.listaPerfis = responsePerfis.data;

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