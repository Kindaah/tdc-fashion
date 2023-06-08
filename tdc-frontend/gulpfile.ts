/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

/**
 * @author Daniel Mejia
 * @file gulpfile.ts
 * @description Gulp tasks
*/

// Dependencies
const gulp = require('gulp');
const { argv } = require('yargs');
const sonarqubeScanner = require('sonarqube-scanner');
const awspublish = require('gulp-awspublish');
const cloudfront = require('gulp-cloudfront-invalidate-aws-publish');
const parallelize = require('concurrent-transform');
const gulpLoadPlugins = require('gulp-load-plugins');
const gulpUtil = require('gulp-util');

// App information
const projectInformation = require('./package.json');

// Gulp plugins
const plugins = gulpLoadPlugins();

// Sonarqube parameters
const sonarqubeURL = 'http://apolo:9000';
const sonarqubeKey = 'e5af06faac9d06ca71a2c0cd85cecb1650603e9b';
const sonarOptions = {
  options : {
    'sonar.host.url': process.env.SONAR_HOST_URL ? process.env.SONAR_HOST_URL : sonarqubeURL,
    'sonar.login': process.env.SONAR_LOGIN ? process.env.SONAR_LOGIN : sonarqubeKey,
    'sonar.buildbreaker.alternativeServerUrl': process.env.SONAR_HOST_URL ? process.env.SONAR_HOST_URL : sonarqubeURL,
    'sonar.projectKey': projectInformation.name,
    'sonar.projectVersion': projectInformation.version,
    'sonar.sources': 'src',
    'sonar.sourceEncoding': 'UTF-8',
    'sonar.typescript.lcov.reportPaths': 'coverage/lcov.info',
    'sonar.verbose': 'true'
  }
};

/**
 * Package the project prod version.
 */
gulp.task('package', () => {
  return gulp.src('dist/**/*')
    .pipe(plugins.tar(`${projectInformation.name}-${projectInformation.version}.tar`))
    .pipe(gulp.dest('package'))
    .on('error', gulpUtil.log);
});

/**
 * Deploy project specified enviroment
 */
gulp.task('deploy', () => {
  const environment = argv.environment;

  const localConfig = {
    buildSrc: `./dist/${projectInformation.name}/**/*`,
    getAwsConf: (env) => {
      const conf = require('./config/aws')[env];

      if (!conf['s3']) {
        throw new Error(`No aws conf for env: ${env}`);
      }

      if (!conf['s3Headers']) {
        throw new Error(`No aws headers for env: ${env}`);
      }

      return { keys: conf['s3'], headers: conf['s3Headers'], cf: conf['cf']};
    }
  };

  const awsConf = localConfig.getAwsConf(environment);
  const publisher = awspublish.create(awsConf.keys);

  return gulp.src(localConfig.buildSrc)
    .pipe(awspublish.gzip({ext: ''}))
    .pipe(parallelize(publisher.publish(awsConf.headers), 10))
    .pipe(cloudfront(awsConf.cf))
    .pipe(publisher.sync())
    .pipe(awspublish.reporter());
});

gulp.task('sonarqube', (callback) => {
  sonarqubeScanner(sonarOptions, callback);
});
