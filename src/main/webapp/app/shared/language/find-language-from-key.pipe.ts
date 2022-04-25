import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'findLanguageFromKey' })
export class FindLanguageFromKeyPipe implements PipeTransform {
  private languages: { [key: string]: { name: string; rtl?: boolean } } = {
    al: { name: 'Shqip' },
    'ar-ly': { name: 'العربية', rtl: true },
    en: { name: 'English' },
    et: { name: 'Eesti' },
    fa: { name: 'فارسی', rtl: true },
    fr: { name: 'Français' },
    // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
  };

  transform(lang: string): string {
    return this.languages[lang].name;
  }

  isRTL(lang: string): boolean {
    return Boolean(this.languages[lang].rtl);
  }
}
