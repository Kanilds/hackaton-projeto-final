angular.module("hackaton-stefanini").controller("PerfilIncluirAlterarController", PerfilIncluirAlterarController);
PerfilIncluirAlterarController.$inject = [
    "$rootScope",
    "$scope",
    "$location",
    "$q",
    "$filter",
    "$routeParams",
    "HackatonStefaniniService"];

function PerfilIncluirAlterarController(
    $rootScope,
    $scope,
    $location,
    $q,
    $filter,
    $routeParams,
    HackatonStefaniniService) {

    /**ATRIBUTOS DA TELA */
    vm = this;
    vm.perfil = {
        id: null,
        nome: "",
        descricao: "",
        dataHoraInclusao: "",
        dataHoraAlteracao: ""
    };

    vm.data = {
        dataHoraInclusao: "",
        dataHoraAlteracao: ""
    }

    vm.urlPerfil = "http://localhost:8080/treinamento/api/perfis/";
    vm.urlPessoa = "http://localhost:8080/treinamento/api/pessoas/";

    /**METODOS DE INICIALIZACAO */
    vm.init = function () {
        vm.tituloTela = "Cadastrar Perfil";
        vm.acao = "Cadastrar";

        vm.listar(vm.urlPerfil).then(
            function (response) {
                if (response !== undefined) {
                    vm.listaPerfil = response;
                    vm.idPerfil = $routeParams;
                    if (vm.idPerfil !== null) {
                        vm.tituloTela = "Editar Perfil";
                        vm.acao = "Editar";

                        vm.recuperarObjetoPorIDURL($routeParams, vm.urlPerfil).then(
                            function (perfilRetorno) {
                                if (perfilRetorno !== undefined) {
                                    vm.perfil = perfilRetorno;
                                }
                            }
                        );
                    }
                }
            }
        );
    };

    /**METODOS DE TELA */
    vm.cancelar = function () {
        vm.retornarTelaListagem();
    };

    vm.retornarTelaListagem = function () {
        $location.path("listarPerfis");
    };

    vm.limparTela = function () {
        $("#modalEndereco").modal("toggle");
        vm.endereco = undefined;
    };

    vm.incluir = function () {

        vm.perfil.dataHoraInclusao = vm.DataJava();
        vm.perfil.dataHoraAlteracao = vm.DataJava();


        var objetoDados = angular.copy(vm.perfil);
        console.log(objetoDados);

        if (vm.acao === "Cadastrar") {
            vm.salvar(vm.urlPerfil, objetoDados).then(
                function (perfilRetorno) {
                    alert("Perfil Cadastrado com sucesso!");
                    vm.retornarTelaListagem();
                });
        } else if (vm.acao === "Editar") {
            vm.alterar(vm.urlPerfil, objetoDados).then(
                function (perfilRetorno) {
                    alert("Perfil Editado com sucesso!");
                    vm.retornarTelaListagem();
                });
        }
    };

    vm.remover = function (objeto, tipo) {

        var url = vm.urlPerfil + objeto.id;
        vm.excluir(url).then(
            function (ojetoRetorno) {
                vm.retornarTelaListagem();
            });
    };

    /**METODOS DE SERVICO */
    vm.recuperarObjetoPorIDURL = function (id, url) {
        var deferred = $q.defer();
        HackatonStefaniniService.listarId(url + id).then(
            function (response) {
                if (response.data !== undefined)
                    deferred.resolve(response.data);
                else
                    deferred.resolve(vm.perfil);
            }
        );
        return deferred.promise;
    };

    vm.listar = function (url) {

        var deferred = $q.defer();
        HackatonStefaniniService.listar(url).then(
            function (response) {
                if (response.data !== undefined) {
                    deferred.resolve(response.data);
                }
            }
        );
        return deferred.promise;
    }

    vm.salvar = function (url, objeto) {

        var deferred = $q.defer();
        var obj = JSON.stringify(objeto);
        HackatonStefaniniService.incluir(url, obj).then(
            function (response) {
                if (response.status == 200) {
                    deferred.resolve(response.data);
                }
            }
        );
        return deferred.promise;
    }

    vm.alterar = function (url, objeto) {

        var deferred = $q.defer();
        var obj = JSON.stringify(objeto);
        HackatonStefaniniService.alterar(url, obj).then(
            function (response) {
                if (response.status == 200) {
                    deferred.resolve(response.data);
                    alert("Salvo com sucesso");
                }
            }
        );
        return deferred.promise;
    }

    vm.excluir = function (url, objeto) {

        var deferred = $q.defer();
        HackatonStefaniniService.excluir(url).then(
            function (response) {
                if (response.status == 200) {
                    deferred.resolve(response.data);
                    alert("Excluido com sucesso")
                }
            }
        );
        return deferred.promise;
    }

    /**METODOS AUXILIARES */

    vm.DataJava = function () {
        var data = new Date();
        data = data.toJSON();

        return data;
    };
}