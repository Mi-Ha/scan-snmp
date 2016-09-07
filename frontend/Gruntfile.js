module.exports = function(grunt) {

    grunt.initConfig({
      nggettext_extract: {
        pot: {
          files: {
            'po/template.pot': ['src/main/resources/frontend/views/*.html','src/main/resources/frontend/**/*.js']

          }
        }
      },
      nggettext_compile: {
        all: {
          files: {
            'src/main/resources/frontend/js/translations.js': ['po/*.po']
          }
        }
      }

    });


  grunt.loadNpmTasks('grunt-angular-gettext');

    grunt.registerTask('default', ['nggettext_extract', 'nggettext_compile']);
};

